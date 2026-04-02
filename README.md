JOB APPLICATION MICROSERVICES SYSTEM

A robust, scalable Job Application System built using a Microservices Architecture with Spring Boot and Spring Cloud.

This project demonstrates industry-level practices such as service discovery, centralized configuration, API gateway routing, distributed tracing, fault tolerance, and event-driven communication.

---

ARCHITECTURE OVERVIEW

The system is divided into domain-based microservices and supporting infrastructure services.

MICROSERVICES

* Job Service (jobms)
  Manages job postings, descriptions, and requirements.

* Company Service (companyms)
  Handles company profiles and organization details.

* Review Service (reviewms)
  Manages user reviews and ratings for companies.

---

INFRASTRUCTURE SERVICES

* Service Registry (Eureka)
  Enables dynamic service discovery.

* API Gateway
  Single entry point for all client requests (routing + security).

* Config Server
  Centralized configuration management.

* Zipkin & Micrometer
  Distributed tracing and monitoring.

* RabbitMQ
  Event-driven asynchronous communication.

* PostgreSQL
  Dedicated database per microservice.

---

TECH STACK

Backend: Java 17, Spring Boot 3.3.5
Microservices: Spring Cloud (Eureka, Gateway, Config Server, OpenFeign)
Database: PostgreSQL, H2 (testing)
Messaging: RabbitMQ
Observability: Zipkin, Micrometer, Actuator
Resilience: Resilience4j
Containerization: Docker, Docker Compose
Build Tool: Maven

---

KEY FEATURES

* Service Discovery using Eureka
* Centralized Configuration with Spring Cloud Config
* Circuit Breaker with Resilience4j
* Distributed Tracing using Zipkin
* Synchronous Communication via OpenFeign
* Asynchronous Communication using RabbitMQ
* Database per Service (loose coupling)
* Containerized Deployment with Docker

---

SYSTEM DESIGN HIGHLIGHTS

* Domain-Driven Design (DDD)
* Loose Coupling & High Cohesion
* Horizontal Scalability
* Event-Driven Architecture

---

GETTING STARTED

PREREQUISITES

* Java 17+
* Maven 3.x
* Docker & Docker Compose

---

RUN WITH DOCKER

1. Clone the repository
   git clone https://github.com/krrobincook/Job-application-microservices.git

2. Navigate into project
   cd Job-application-microservices

3. Build all services
   mvn clean install

4. Start containers
   docker-compose up --build

---

ACCESS SERVICES

API Gateway: http://localhost:8080
Eureka Dashboard: http://localhost:8761
Zipkin: http://localhost:9411

---

SERVICE COMMUNICATION

Synchronous:

* REST APIs using OpenFeign

Asynchronous:

* Event-driven messaging via RabbitMQ

---

OBSERVABILITY

* Zipkin for request tracing
* Actuator + Micrometer for monitoring
