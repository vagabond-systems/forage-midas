# Midas Core: Transaction Processing Microservice

A Spring Boot microservice that takes in financial transactions from Kafka, validates them against user balances, applies incentives from an external REST API, saves the results to a SQL database, and serves user balances through a REST endpoint.

I built this for the **J.P. Morgan Chase Advanced Software Engineering Job Simulation** on [Forage](https://www.theforage.com/). Forage provided the starter project and the test harness. The service logic described below is my own work.

## Tech stack

- **Java 17**, **Spring Boot 3.2**
- **Apache Kafka** (Spring Kafka, embedded Kafka for tests)
- **Spring Data JPA** with an **H2** in-memory SQL database
- **RestTemplate** for calls to the external Incentive API
- **Maven** for the build and test suites

## What it does

```
Kafka topic ──► TransactionListener ──► validate (sender/recipient exist, sufficient funds)
                                         │
                                         ├─► POST /incentive  (external Incentive API)
                                         │
                                         ├─► update sender & recipient balances (UserRecord)
                                         └─► persist TransactionRecord
                                                         │
                         GET /balance?userId=N ◄─────────┘  (BalanceController)
```

1. **Kafka ingestion.** `TransactionListener` reads from a configurable topic (`general.kafka-topic`) and turns JSON messages into `Transaction` objects.
2. **Validation and persistence.** The listener rejects a transaction when the sender or recipient doesn't exist or when the sender's balance is too low. Valid transactions update both users' balances and are saved as a `TransactionRecord` entity, which has `@ManyToOne` relations to `UserRecord`.
3. **Incentive integration.** For each valid transaction, the listener POSTs to the Incentive API and adds the returned incentive amount to the recipient's balance.
4. **Balance API.** `GET /balance?userId={id}` returns `{"amount": ...}` for a user, or `0` if the user doesn't exist.

## Project structure

```
src/main/java/com/jpmc/midascore/
├── component/    TransactionListener (Kafka consumer + business logic), DatabaseConduit
├── controller/   BalanceController (REST endpoint)
├── entity/       UserRecord, TransactionRecord (JPA entities)
├── foundation/   Transaction, Incentive, Balance (DTOs)
└── repository/   UserRepository, TransactionRecordRepository
```

## Running it

Requirements: Java 17+.

```bash
# 1. Start the Incentive API (provided jar, listens on :8080)
java -jar services/transaction-incentive-api.jar

# 2. In another terminal, run the test suites
./mvnw test
```

The service runs on port `33400`. Its configuration is in `application.yml`.

## Tests

`TaskOneTests` through `TaskFiveTests` use embedded Kafka to send sample transaction files through the service, then check the resulting balances in the database and through the REST API.
