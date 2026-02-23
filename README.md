# Midas Core
Project repo for the JPMC Advanced Software Engineering Forage program.

## Prerequisites
1. Install Java 17.
2. Use the Maven wrapper included in this repo (`mvnw` or `mvnw.cmd`), so you do not need a global Maven install.
3. Use an IDE with debugging support (IntelliJ or VS Code Java extensions).

## Project Setup
1. Open a terminal in `forage-midas`.
2. Build once to download dependencies:
```powershell
./mvnw clean test -DskipTests
```
3. Keep all commands in this README run from `forage-midas`.

## Task 1
1. Run:
```powershell
./mvnw "-Dtest=TaskOneTests" test
```
2. Find the log block between:
`---begin output ---`
and
`---end output ---`
3. Submit that exact output for Task 1.

## Task 2
1. Run:
```powershell
./mvnw "-Dtest=TaskTwoTests" test
```
2. Put a breakpoint in `src/main/java/com/jpmc/midascore/component/TransactionListener.java` inside `receive(...)`.
3. Debug the test and inspect incoming `Transaction` messages.
4. The test is intentionally long-running; stop it manually after you find the required answer.

## Task 3
1. Run:
```powershell
./mvnw "-Dtest=TaskThreeTests" test
```
2. Put a breakpoint in `src/main/java/com/jpmc/midascore/component/DatabaseConduit.java` inside `processTransaction(...)`.
3. Debug and track user balances as each transaction is processed.
4. Identify Waldorf's final balance.
5. Stop the test manually after you capture the answer.

## Task 4
Task 4 requires the incentive API to be running locally.

1. Start the incentive API in terminal A:
```powershell
java -jar services/transaction-incentive-api.jar
```
2. In terminal B, run:
```powershell
./mvnw "-Dtest=TaskFourTests" test
```
3. Debug with breakpoints in `DatabaseConduit.processTransaction(...)`.
4. Track incentive + transfer updates and identify Wilbur's final balance.
5. Stop the test manually once you have the answer.
6. Stop the incentive API process when done.

## Task 5
1. Start the incentive API if it is not already running:
```powershell
java -jar services/transaction-incentive-api.jar
```
2. Run:
```powershell
./mvnw "-Dtest=TaskFiveTests" test
```
3. Copy the output between:
`---begin output ---`
and
`---end output ---`
4. Submit that output for Task 5.

## Useful Commands
Run all tests:
```powershell
./mvnw test
```

Run a single test class:
```powershell
./mvnw "-Dtest=TaskThreeTests" test
```

## Ports Used
- `33400`: Midas Core Spring Boot app
- `9092`: Embedded Kafka (in tests)
- `8080`: Incentive API (`transaction-incentive-api.jar`)
