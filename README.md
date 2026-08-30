# JPMorgan Chase & Co. — Software Engineering Job Simulation

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![H2](https://img.shields.io/badge/H2_Database-004088?style=for-the-badge&logo=h2&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

*Completed as part of the JPMorgan Chase & Co. Software Engineering Job Simulation on [Forage](https://www.theforage.com/) — May 2026*

</div>

---

## 📌 Overview

This repository contains my work from the **JPMorgan Chase & Co. Software Engineering Job Simulation** on Forage. The simulation provided hands-on experience working with a real-world-style Spring Boot backend — involving Kafka-based event streaming, H2 database integration, and REST API development.

The project simulates the kind of backend engineering work done at JPMorgan Chase, giving practical exposure to enterprise-grade tools and workflows.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Primary backend language |
| Spring Boot | Application framework |
| Apache Kafka | Event-driven message streaming |
| H2 Database | In-memory database for dev/testing |
| REST API | Backend communication layer |
| Maven | Build tool & dependency management |
| JUnit | Unit and integration testing |

---

## 📂 Project Structure

```text
forage-midas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jpmc/midascore/
│   │   │       ├── foundation/        ← Core domain models
│   │   │       ├── repository/        ← Data access layer (JPA)
│   │   │       ├── service/           ← Business logic
│   │   │       └── controller/        ← REST API endpoints
│   │   └── resources/
│   │       └── application.yml        ← App configuration (Kafka, DB)
│   └── test/
│       └── java/
│           └── com/jpmc/midascore/    ← JUnit tests (embedded Kafka)
├── pom.xml                            ← Maven dependencies
└── mvnw                               ← Maven wrapper
```

---

## 🔧 What I Built

### 1. 🟡 Kafka Integration
Set up Apache Kafka for event-driven transaction handling — configured topics, producers, and consumers. Tested using embedded Kafka to verify message flow end-to-end.

**Key concepts:** Kafka topics · Producers & consumers · Message handling · Embedded Kafka testing

### 2. 🗄️ H2 Database Integration
Integrated an H2 in-memory database using Spring Data JPA to persist and retrieve transaction data during development and testing — no external DB setup needed.

### 3. 🔌 REST API Integration
Connected application components through REST-based communication, enabling modular service interaction within the backend system.

### 4. 🎮 REST API Controller
Implemented controller-level logic to expose backend operations as HTTP endpoints — handling request mapping, response structuring, and service delegation.

### 5. ✅ Testing & Verification
Wrote and ran Spring Boot integration tests with embedded Kafka to verify implemented functionality and catch configuration issues before deployment.

---

## 🚀 Getting Started

### Prerequisites

- Java 17+ installed
- Maven (or use the included Maven Wrapper `mvnw`)
- Any Java IDE — IntelliJ IDEA recommended

### Clone the Repository

```bash
git clone https://github.com/vivek28n/<repo-name>.git
cd forage-midas
```

### Build the Project

```bash
# Using Maven Wrapper (Windows)
mvnw.cmd clean compile

# Using Maven Wrapper (Mac/Linux)
./mvnw clean compile

# If Maven is installed globally
mvn clean compile
```

### Run Tests

```bash
mvnw.cmd test        # Windows
./mvnw test          # Mac/Linux
```

### Run the Application

```bash
mvnw.cmd spring-boot:run        # Windows
./mvnw spring-boot:run          # Mac/Linux
```

The application will start on `http://localhost:8080`

---

## 📚 Key Learnings

| Area | What I Learned |
|---|---|
| Spring Boot | Understanding and extending an existing codebase |
| Kafka | Event-driven architecture, producer-consumer pattern |
| Database | JPA repositories, in-memory DB integration |
| REST API | Endpoint design, controller-service-repository pattern |
| Testing | Integration testing with embedded Kafka, JUnit |
| DevOps | Maven build lifecycle, application configuration (YAML) |

---

## 🏆 Certification

**JPMorgan Chase & Co. — Software Engineering Job Simulation**  
Issued by **Forage** · Completed **May 2026**

Tasks completed:
- ✅ Project Setup & Codebase Understanding
- ✅ Kafka Integration
- ✅ H2 Database Integration
- ✅ REST API Integration
- ✅ REST API Controller Implementation

---

## 📜 Disclaimer

This project was completed as part of a **virtual job simulation hosted by Forage in partnership with JPMorgan Chase & Co.** It is a learning exercise and does not represent actual employment or proprietary work of JPMorgan Chase & Co.

---

## 👨‍💻 Author

**Vivek Nigam**  
B.Tech CSE (Artificial Intelligence) · NIET Greater Noida · 2024–2028

[![GitHub](https://img.shields.io/badge/GitHub-vivek28n-181717?style=flat&logo=github)](https://github.com/vivek28n)

---

> ⭐ Found this useful? Star the repo and feel free to connect on LinkedIn!
