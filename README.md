# Midas

A Spring Boot transaction processing system built for the JPMC Advanced Software Engineering Forage program. Ingests financial transactions via Kafka, validates and processes them with an external incentive API, and exposes user balances through a REST endpoint.

## Architecture

```
Kafka (trader-updates topic)
       │
       ▼
┌─────────────────────────┐
│ TransactionListener      │  ← consumes + validates
│   │                      │
│   ├── IncentiveClient ───│──→ POST /incentive
│   │                      │      ← incentive bonus
│   ▼                      │
│ Update balances          │  ← debit sender, credit recipient + incentive
│   ▼                      │
│ Save to DB (H2/JPA)      │
└─────────────────────────┘
       │
       ▼
┌─────────────────────────┐
│ BalanceController        │  ← GET /balance?userId=X (port 33400)
│   ↕                      │
│ UserRepository           │
└─────────────────────────┘
```

## Tech Stack

- **Java 17** / **Spring Boot 3.2.5** (Web, Data JPA, Kafka)
- **H2** in-memory database
- **Apache Kafka** (Embedded Kafka for tests)
- **Maven** wrapper build

## Getting Started

```bash
# Build
./mvnw clean install -DskipTests

# Start the Incentive API (required for transaction processing)
java -jar services/transaction-incentive-api.jar &

# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test
```

## API

### `GET /balance?userId={id}`

Returns the user's current balance.

```json
{"amount": 1326.98}
```

Returns `{"amount": 0.0}` if the user does not exist.

## Project Structure

```
src/
├── main/java/com/jpmc/midascore/
│   ├── MidasCoreApplication.java     ← Spring Boot entry point
│   ├── component/
│   │   ├── DatabaseConduit.java      ← DB save helper
│   │   └── IncentiveClient.java      ← REST client for incentive API
│   ├── config/
│   │   └── KafkaConfig.java          ← Kafka producer/consumer factories
│   ├── controller/
│   │   └── BalanceController.java    ← GET /balance endpoint
│   ├── entity/
│   │   ├── UserRecord.java           ← User JPA entity
│   │   └── TransactionRecord.java    ← Transaction JPA entity
│   ├── foundation/
│   │   ├── Balance.java              ← Balance DTO
│   │   ├── Incentive.java            ← Incentive API response DTO
│   │   └── Transaction.java          ← Transaction DTO
│   ├── listener/
│   │   └── TransactionListener.java  ← Kafka consumer + processor
│   └── repository/
│       ├── TransactionRecordRepository.java
│       └── UserRepository.java
├── test/java/com/jpmc/midascore/
│   ├── TaskOneTests.java .. TaskFiveTests.java
│   ├── KafkaProducer.java            ← Test Kafka producer
│   ├── FileLoader.java               ← Loads test data files
│   ├── UserPopulator.java            ← Seeds test users
│   └── BalanceQuerier.java           ← Test REST client
└── test/resources/test_data/         ← CSV transaction + user data
```

## How It Works

1. **Ingestion** — Transactions arrive on the `trader-updates` Kafka topic
2. **Validation** — Sender and recipient must exist; sender must have sufficient balance
3. **Incentive** — Valid transactions are posted to the Incentive API for a bonus amount
4. **Processing** — Sender is debited; recipient is credited (transaction amount + incentive)
5. **Persistence** — Both user balances and a transaction record are saved to the database
6. **Query** — Users can check balances via the REST endpoint
