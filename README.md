# JPMorganChase - Advanced Software Engineering Virtual Experience

This repository contains my implementation of key backend microservices for JPMorganChase's **Midas** transaction-processing platform, completed as part of the **JPMorganChase Advanced Software Engineering Simulation** via Forage.

## 🛠️ Tech Stack & Key Technologies
* **Language:** Java 17
* **Framework:** Spring Boot, Spring Web (REST APIs), Spring Data JPA
* **Messaging & Streaming:** Apache Kafka, Embedded Kafka
* **Database:** H2 In-Memory Relational Database, SQL
* **Build & Dev Tools:** Maven, Postman, RestTemplate

---

## 🚀 System Architecture & Key Modules Implemented

### 1. Event Streaming & Kafka Integration (`Task 1 & 2`)
* Configured and integrated **Apache Kafka** into the Spring Boot microservice to ingest and deserialize real-time, high-volume transaction messages.
* Utilized an embedded Kafka test framework to validate consumer reliability and topic configurations.
* Resolved Maven dependency conflicts and cleaned up Java null-safety warnings to maintain clean build pipelines.

### 2. Database Persistence & Entity Modeling (`Task 3`)
* Integrated an **H2 relational database** with **Spring Data JPA** to validate and record transaction data.
* Designed entity models and persistence logic to automate balance updates across user records upon processing incoming streams.

### 3. External Incentive REST API Integration (`Task 4`)
* Connected the Spring Boot backend to an external **REST Incentive API** using `RestTemplate`.
* Processed external API responses and dynamically incorporated incentive rewards into ongoing transactional workflows.

### 4. Financial REST API Controller (`Task 5`)
* Developed RESTful API endpoints via a Spring Controller to expose user balances and financial transaction data in structured JSON format.
* Maintained clean architectural separation of concerns across controller, service, and repository layers.
