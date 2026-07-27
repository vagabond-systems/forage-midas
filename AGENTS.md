# AGENTS.md - JPMC Midas Core (Forage Program)

## Project Overview
- **Type**: Spring Boot 3.2.5 (Java 17) monorepo for JPMC Advanced Software Engineering Forage program
- **Package**: `com.jpmc.midascore` (artifact: `midas-core`)
- **Database**: H2 (runtime), JPA
- **Messaging**: Spring Kafka + EmbeddedKafka for tests
- **Build**: Maven Wrapper (`./mvnw`)

## Key Commands
```bash
./mvnw test              # Run all tests (includes EmbeddedKafka)
./mvnw test -Dtest=TaskOneTests  # Run single test class
./mvnw spring-boot:run   # Run application
./mvnw clean package     # Build JAR (outputs to target/)
```

## Test Structure
- Tests are in `src/test/java/com/jpmc/midascore/Task*Tests.java` (TaskOne through TaskFive)
- Uses `@EmbeddedKafka` + `@SpringBootTest` + `@DirtiesContext`
- Tests use embedded Kafka on `localhost:9092` (port 9092)
- Some tests (e.g., `TaskTwoTests`) are designed for manual debugger inspection - they loop indefinitely
- Test data files in `src/test/resources/test_data/`
- Run single test: `./mvnw test -Dtest=TaskOneTests`

## Project Structure
```
src/main/java/com/jpmc/midascore/
  ├── MidasCoreApplication.java       # Spring Boot entry point
  ├── component/DatabaseConduit.java  # DB operations
  ├── entity/UserRecord.java          # JPA entity
  ├── foundation/                     # Balance, Transaction domain objects
  └── repository/UserRepository.java  # JPA repository
services/
  └── transaction-incentive-api.jar   # External JAR dependency (pre-built)
src/test/java/com/jpmc/midascore/
  ├── TaskOneTests...TaskFiveTests    # Task tests (Forage tasks 1-5)
  ├── KafkaProducer, FileLoader, BalanceQuerier, UserPopulator (test utilities)
  └── resources/test_data/            # Test data files
```

## Key Conventions
- **Java 17**, Spring Boot 3.2.5, Maven Wrapper
- Package: `com.jpmc.midascore` (not `com.jpmc.midascore.*` for main)
- H2 in-memory DB (runtime scope)
- Embedded Kafka for integration tests (port 9092)
- Kafka topic: `trader-updates` (from `application.yml`)

## Test Notes
- Tests use `@DirtiesContext` + `@EmbeddedKafka` - run sequentially/isolated
- `TaskTwoTests.task_two_verifier()` loops infinitely - designed for debugger inspection
- Test data in `src/test/resources/test_data/` (e.g., `poiuytrewq.uiop`)
- Use `./mvnw test -Dtest=TaskOneTests` to run single task

## Build Artifacts
- `target/` - Maven build output (gitignored)
- `services/transaction-incentive-api.jar` - Pre-built external dependency (gitignored except this file)

## Environment
- Java 17 required
- Uses Maven Wrapper (`./mvnw`) - no local Maven install needed
- H2 console not enabled by default
- Kafka test port: 9092 (embedded)