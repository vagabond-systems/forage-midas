# Midas Core

A Spring Boot application for processing financial transactions with Kafka integration, database persistence, external API integration, and REST API endpoints.

## Project Overview

Midas Core is a transaction processing system that:
- Receives transactions from Kafka message queues
- Validates transactions (sender/recipient existence, sufficient balance)
- Integrates with an external incentive API to calculate bonuses
- Persists transactions and updates user balances in an H2 database
- Exposes a REST API endpoint for querying user balances

## Architecture

### Components

1. **Kafka Listener** - Receives and processes transactions from Kafka
2. **Database Layer** - Stores users and transactions using Spring Data JPA
3. **Incentive Service** - Calls external incentive API via RestTemplate
4. **REST Controller** - Exposes balance query endpoint

### Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.2.5** - Application framework
- **Spring Kafka** - Kafka integration
- **Spring Data JPA** - Database abstraction
- **H2 Database** - In-memory database
- **Maven** - Build tool
- **Kafka** - Message queue system

## Project Structure

```
forage-midas/
├── src/
│   ├── main/
│   │   ├── java/com/jpmc/midascore/
│   │   │   ├── component/
│   │   │   │   ├── DatabaseConduit.java
│   │   │   │   └── TransactionListener.java
│   │   │   ├── config/
│   │   │   │   └── RestTemplateConfig.java
│   │   │   ├── controller/
│   │   │   │   └── BalanceController.java
│   │   │   ├── entity/
│   │   │   │   ├── TransactionRecord.java
│   │   │   │   └── UserRecord.java
│   │   │   ├── foundation/
│   │   │   │   ├── Balance.java
│   │   │   │   ├── Incentive.java
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/
│   │   │   │   ├── TransactionRecordRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   └── IncentiveService.java
│   │   │   └── MidasCoreApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       ├── java/com/jpmc/midascore/
│       │   ├── FileLoader.java
│       │   ├── KafkaProducer.java
│       │   ├── UserPopulator.java
│       │   ├── BalanceQuerier.java
│       │   ├── TaskOneTests.java
│       │   ├── TaskTwoTests.java
│       │   ├── TaskThreeTests.java
│       │   ├── TaskFourTests.java
│       │   └── TaskFiveTests.java
│       └── resources/
│           └── test_data/
├── services/
│   └── transaction-incentive-api.jar
├── application.yml
├── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites

- Java 17 (OpenJDK Temurin)
- Maven 3.9.16
- Git

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Ashikvk18/forage-midas.git
cd forage-midas
```

2. Set JAVA_HOME environment variable:
```bash
export JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
```

3. Add Java and Maven to PATH:
```bash
export PATH="$JAVA_HOME/bin:$PATH"
```

## Configuration

### application.yml

The application is configured in `application.yml`:

```yaml
general:
  kafka-topic: trader-updates
  incentive-api-url: http://localhost:8080/incentive

server:
  port: 33400

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true
  kafka:
    consumer:
      group-id: midas-core-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

## Running the Application

### Build the Project

```bash
./mvnw clean install
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on port 33400.

### Start the Incentive API

The incentive API is required for transaction processing:

```bash
java -jar services/transaction-incentive-api.jar
```

The incentive API runs on port 8080.

## Running Tests

### Run All Tests

```bash
./mvnw test
```

### Run Specific Test

```bash
./mvnw test -Dtest=TaskOneTests
./mvnw test -Dtest=TaskTwoTests
./mvnw test -Dtest=TaskThreeTests
./mvnw test -Dtest=TaskFourTests
./mvnw test -Dtest=TaskFiveTests
```

**Note:** TaskFourTests and TaskFiveTests require the incentive API to be running.

## API Endpoints

### GET /balance

Query a user's balance by ID.

**Request:**
```
GET http://localhost:33400/balance?userId=5
```

**Response:**
```json
{
  "amount": 1234.56
}
```

If the user doesn't exist, returns:
```json
{
  "amount": 0.0
}
```

## Transaction Processing Flow

1. **Kafka sends transaction** to the configured topic
2. **TransactionListener receives** the transaction
3. **Validation checks:**
   - Sender exists in database
   - Recipient exists in database
   - Sender has sufficient balance
4. **Incentive API call** to calculate bonus amount
5. **Database persistence:**
   - Save transaction with incentive amount
   - Update sender's balance (subtract amount)
   - Update recipient's balance (add amount + incentive)
6. **Invalid transactions** are discarded

## Database Schema

### UserRecord

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary key (auto-generated) |
| name | String | User name |
| balance | Float | Account balance |

### TransactionRecord

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary key (auto-generated) |
| sender | UserRecord | Sender (many-to-one) |
| recipient | UserRecord | Recipient (many-to-one) |
| amount | Float | Transaction amount |
| incentive | Float | Incentive amount |

## Task Completion Summary

### Task 1: Project Setup
- Configured Maven dependencies
- Set up Spring Boot application
- Verified build and test execution

### Task 2: Kafka Integration
- Implemented TransactionListener to consume Kafka messages
- Configured Kafka topic and serialization
- Successfully received and logged transactions

### Task 3: Database Integration
- Configured H2 in-memory database
- Created TransactionRecord entity with JPA
- Implemented transaction validation logic
- Persisted valid transactions and updated balances
- Task 3 Answer: 627 (waldorf's final balance)

### Task 4: External API Integration
- Created IncentiveService to call incentive API
- Implemented RestTemplate configuration
- Integrated incentive calculation into transaction processing
- Added incentive amounts to recipient balances
- Task 4 Answer: 3089 (wilbur's final balance)

### Task 5: REST API Endpoint
- Created BalanceController with /balance endpoint
- Configured application to run on port 33400
- Implemented user balance query functionality
- Task 5 Output: Balance data for users 0-12

## Dependencies

Key dependencies in `pom.xml`:

- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-kafka
- h2
- spring-boot-starter-test
- spring-kafka-test
- testcontainers (kafka, junit-jupiter)

## Troubleshooting

### Port Already in Use

If you get "Address already in use" error:

```bash
# Find process using the port
netstat -ano | findstr :8080

# Stop the process
Stop-Process -Id <PROCESS_ID> -Force
```

### JAVA_HOME Not Set

If you get JAVA_HOME errors:

```bash
# Set JAVA_HOME temporarily
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"

# Or set permanently in System Environment Variables
```

### Maven Not Found

If Maven is not found:

```bash
# Add Maven to PATH
$env:PATH += ";C:\Program Files\Apache\maven\apache-maven-3.9.16\bin"
```

## Certificate

Completion certificate for the JPMorgan Chase Advanced Software Engineering Forage program:

[![Certificate](certificate.pdf)](certificate.pdf)

## Contributing

This project is part of the JPMorgan Chase Advanced Software Engineering Forage program.

## License

This project is for educational purposes as part of the JPMorgan Chase Forage program.

## Contact

For questions about this project, please refer to the JPMorgan Chase Forage program resources.
