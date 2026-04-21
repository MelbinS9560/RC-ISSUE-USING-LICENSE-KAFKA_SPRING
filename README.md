# RTO System: License & RC Management (Kafka Microservices)

This project is a distributed microservices application built with **Java 21** and **Spring Boot**, utilizing **Apache Kafka** for asynchronous communication. It simulates an RTO (Regional Transport Office) workflow where issuing a license triggers the creation of a Vehicle RC.

## 🏗 Architecture Overview

The project consists of two main microservices:

1.  **License-Services (Producer):** * Handles license registration.
    * Once a license is created, it publishes a message to the Kafka topic `license-topic`.
2.  **RC-Services (Consumer):** * Listens to the `license-topic`.
    * Consumes the license data and automatically generates/issues a corresponding RC (Registration Certificate).

## 🚀 Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Messaging:** Apache Kafka 4.2.0
* **Database:** PostgreSQL (for data persistence)
* **Containerization:** Docker (for Kafka/Zookeeper setup)
* **Environment:** macOS (Intel i9)

## 📁 Project Structure

```text
RC_ISSUE_SPRING_KAFKA/
├── license-services/   # Producer Microservice
├── rc-services/        # Consumer Microservice
└── README.md           # Project Documentation
