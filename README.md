<h1 align="center">
  💼 Job Application Microservices
</h1>

<p align="center">
  <b>A production-ready, cloud-native Job Application Platform built with Spring Boot Microservices</b><br/>
  Showcasing service discovery, centralized config, async messaging, distributed tracing, and fault tolerance.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Spring%20Cloud-2023.x-6DB33F?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" />
  <img src="https://img.shields.io/badge/RabbitMQ-3-FF6600?style=for-the-badge&logo=rabbitmq" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql" />
</p>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [System Architecture](#️-system-architecture)
- [Tech Stack](#-tech-stack)
- [Key Features](#-key-features)
- [Microservices Breakdown](#-microservices-breakdown)
- [Getting Started](#-getting-started)
- [Service URLs](#-service-urls)
- [API Reference](#-api-reference)
- [Resilience & Observability](#️-resilience--observability)
- [Project Structure](#-project-structure)

---

## 🌟 Overview

The **Job Application Microservices Platform** is a fully containerized, distributed backend system that models a real-world job board application. It is designed with microservices best practices at its core — every service owns its domain, communicates through well-defined interfaces, and can be deployed, scaled, and maintained independently.

> This project is ideal for demonstrating backend engineering expertise including distributed systems design, cloud-native development, and DevOps integration.

---

## 🏛️ System Architecture

```
                          ┌─────────────────────────────────────┐
                          │           CLIENT / POSTMAN           │
                          └──────────────┬──────────────────────┘
                                         │ HTTP Request
                                         ▼
                          ┌─────────────────────────────────────┐
                          │           API GATEWAY               │
                          │         (Port: 8084)                │
                          │  • Routing  • Load Balancing        │
                          └──────┬──────────────┬───────────────┘
                                 │              │
               ┌─────────────────▼──┐     ┌────▼──────────────────┐
               │    Config Server   │     │   Service Registry    │
               │    (Port: 8888)    │     │   Eureka (Port: 8761) │
               │  Centralized Conf. │     │   Dynamic Discovery   │
               └────────────────────┘     └───────────────────────┘
                                                     │
          ┌──────────────────────┬───────────────────┴────────────┐
          │                      │                                 │
┌─────────▼────────┐  ┌──────────▼───────┐           ┌───────────▼────────┐
│   Job Service    │  │  Company Service │           │  Review Service    │
│   (jobms)        │  │  (companyms)     │           │  (reviewms)        │
│  Manages jobs    │  │  Company data    │           │  Ratings & Reviews │
└──────┬───────────┘  └──────────────────┘           └────────────────────┘
       │
       │ (Async via RabbitMQ)
       ▼
┌────────────────────────────────────────────────┐
│  Infrastructure: PostgreSQL │ RabbitMQ │ Zipkin │
└────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.3.5 |
| **Microservices** | Spring Cloud (Eureka, Gateway, Config Server, OpenFeign) |
| **Database** | PostgreSQL 16 (Database-per-Service pattern) |
| **Messaging** | RabbitMQ 3 (AMQP) |
| **Tracing** | Zipkin + Micrometer Tracing |
| **Resilience** | Resilience4j (Circuit Breaker) |
| **Containerization** | Docker + Docker Compose |
| **Build Tool** | Maven |
| **Monitoring** | Spring Boot Actuator, pgAdmin 4 |

---

## ✨ Key Features

### 🔍 Service Discovery
Microservices automatically register with **Eureka Server** on startup and use it to locate other services — no hardcoded URLs, fully dynamic.

### ⚙️ Centralized Configuration
All service configurations are managed by the **Spring Cloud Config Server**, pulling from a Git-backed repository. Supports environment-specific profiles (`default`, `docker`).

### 🔄 Dual Communication Model
- **Synchronous**: Direct REST calls via **OpenFeign** (e.g., Job Service → Company Service to enrich job data)
- **Asynchronous**: Event-driven updates via **RabbitMQ** (e.g., company name change automatically propagates to job records)

### 🛡️ Circuit Breaker Pattern
**Resilience4j** protects the system from cascading failures. If the Review Service is unavailable, the Job Service gracefully returns a fallback response — keeping the system operational.

### 📊 Distributed Tracing
Every request is assigned a **Trace ID** that flows across all services. **Zipkin** visualizes the full request lifecycle, making debugging in a distributed system straightforward.

### 🐳 Fully Containerized
One command (`docker-compose up`) spins up the entire platform — all 9 containers, networked and configured.

### 🗄️ Database-per-Service
Each microservice owns its dedicated PostgreSQL schema, ensuring **loose coupling** and enabling independent database evolution.

---

## 📦 Microservices Breakdown

### 1. 🔵 API Gateway (`api-gateway`) — Port `8084`
The single entry point for all external traffic. Routes requests to appropriate services via Eureka-powered load balancing.

### 2. 🟢 Service Registry (`service-registry`) — Port `8761`
Netflix Eureka server. All microservices register here, enabling dynamic service discovery without hardcoded host/port configurations.

### 3. 🟡 Config Server (`configserver`) — Port `8888`
Centralized configuration management fetching properties from a Git repository. Supports live config refresh via Spring Cloud Bus.

### 4. 💼 Job Service (`jobms`)
- CRUD operations for job postings
- Fetches enriched company data from Company Service via **OpenFeign**
- Publishes events to RabbitMQ for async propagation
- Implements **Circuit Breaker** for resilient inter-service calls

### 5. 🏢 Company Service (`companyms`)
- Manages company profiles (name, description, etc.)
- Publishes company update events to **RabbitMQ**

### 6. ⭐ Review Service (`reviewms`)
- Manages user reviews and ratings for companies
- Consumes company update events from RabbitMQ to keep data in sync

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| JDK | 17+ |
| Maven | 3.x |
| Docker | Latest |
| Docker Compose | v2+ |

### ▶️ Run with Docker (Recommended)

**Step 1 — Clone the repository**
```bash
git clone https://github.com/krrobincook/Job-application-microservices.git
cd Job-application-microservices
```

**Step 2 — Set your GitHub Token** *(for Config Server to access the config repo)*
```bash
# Windows (PowerShell)
$env:GITHUB_TOKEN="your_github_token_here"

# Linux / macOS
export GITHUB_TOKEN="your_github_token_here"
```

**Step 3 — Build all service JARs**
```bash
mvn clean package -DskipTests
```

**Step 4 — Launch the entire platform**
```bash
docker-compose up -d
```

> ⏳ Allow ~60 seconds for all services to register with Eureka and become healthy.

**To stop all services:**
```bash
docker-compose down
```

---

## 🌐 Service URLs

| Service | URL | Credentials |
|---|---|---|
| **API Gateway** | http://localhost:8084 | — |
| **Eureka Dashboard** | http://localhost:8761 | — |
| **Config Server** | http://localhost:8888 | — |
| **Zipkin UI** | http://localhost:9411 | — |
| **RabbitMQ Management** | http://localhost:15672 | `guest` / `guest` |
| **pgAdmin** | http://localhost:5050 | `admin@admin.com` / `admin` |

---

## 📡 API Reference

All requests go through the **API Gateway** at `http://localhost:8084`.

### 💼 Jobs
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/jobs` | Fetch all jobs (with company & reviews) |
| `GET` | `/jobs/{id}` | Get a specific job by ID |
| `POST` | `/jobs` | Create a new job posting |
| `PUT` | `/jobs/{id}` | Update a job posting |
| `DELETE` | `/jobs/{id}` | Delete a job posting |

### 🏢 Companies
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/companies` | Fetch all companies |
| `GET` | `/companies/{id}` | Get a specific company |
| `POST` | `/companies` | Register a new company |
| `PUT` | `/companies/{id}` | Update company details |
| `DELETE` | `/companies/{id}` | Remove a company |

### ⭐ Reviews
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/reviews?companyId={id}` | Get all reviews for a company |
| `GET` | `/reviews/{id}` | Get a specific review |
| `POST` | `/reviews?companyId={id}` | Submit a review for a company |
| `PUT` | `/reviews/{id}` | Update a review |
| `DELETE` | `/reviews/{id}` | Delete a review |

---

## 🛡️ Resilience & Observability

### Circuit Breaker (Resilience4j)

The Job Service implements the circuit breaker pattern when calling the Review Service:

```
Normal Flow:     Job Service ──────────► Review Service ✅
Fallback Flow:   Job Service ──✂──────► Review Service ❌ (Unavailable)
                     │
                     └──► Returns fallback response 🛡️
```

States: **CLOSED** → **OPEN** (after threshold failures) → **HALF-OPEN** (recovery probe) → **CLOSED**

### Distributed Tracing with Zipkin

Every request across services is tagged with a shared **Trace ID**:

```
Gateway → Job Service → Company Service → Review Service
  [traceId: abc123]   [abc123]           [abc123]
```

Visit **http://localhost:9411** to visualize and analyze request flows.

### Health Monitoring

All services expose Spring Boot Actuator endpoints:
- `GET /actuator/health` — Service health status
- `GET /actuator/info` — Application metadata

---

## 📁 Project Structure

```
Job-application-microservices/
│
├── 📂 API-Gateway/          # Spring Cloud Gateway — routing & entry point
├── 📂 service-registry/     # Eureka Server — service discovery
├── 📂 configserver/         # Spring Cloud Config Server
├── 📂 jobms/                # Job microservice
├── 📂 companyms/            # Company microservice
├── 📂 reviewms/             # Review microservice
│
├── 🐳 docker-compose.yml    # Orchestrates all 9 containers
├── 🗄️ init-db.sql           # Database initialization script
└── 📄 README.md
```

---

## 🤝 Contributing

Contributions, issues and feature requests are welcome!

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Made with ☕ Java + 🍃 Spring Boot &nbsp;|&nbsp; <b>krrobincook</b>
</p>
