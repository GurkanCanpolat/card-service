# Card Service

A Spring Boot–based card management and transaction processing application demonstrating event-driven architecture, observability, and resilient backend design.

## Current Features

* Customer management
* Card creation and management
* Card transaction processing
* Transaction reversal
* PostgreSQL persistence
* Kafka-based event publishing and consumption
* Transactional outbox processing
* Dead-letter handling
* OpenAPI and Swagger UI
* Spring Boot Actuator
* Prometheus metrics
* Grafana dashboards
* Docker Compose development environment

## Architecture

```text
Client / Swagger
       |
       v
  Card Service
       |
       +--------------------+
       |                    |
       v                    v
  PostgreSQL              Kafka
       |                    |
       v                    v
  Outbox Records        Consumers / DLQ

Card Service
       |
       v
Actuator Metrics
       |
       v
Prometheus
       |
       v
Grafana
```

## Technology Stack

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Apache Kafka
* Docker and Docker Compose
* Micrometer
* Prometheus
* Grafana
* Maven
* OpenAPI / Swagger

## Running Locally

Create the local environment file:

```bash
cp .env.example .env
```

Start the infrastructure and application:

```bash
docker compose up -d --build
```

Check container status:

```bash
docker compose ps
```

## Application Endpoints

| Component          | Address                                       |
| ------------------ | --------------------------------------------- |
| Application        | `http://localhost:8080`                       |
| Swagger UI         | `http://localhost:8080/swagger-ui/index.html` |
| Actuator Health    | `http://localhost:8080/actuator/health`       |
| Prometheus Metrics | `http://localhost:8080/actuator/prometheus`   |
| Prometheus         | `http://localhost:9090`                       |
| Grafana            | `http://localhost:3000`                       |

## Monitoring

The application exposes JVM, HTTP, process, and custom business metrics through Micrometer and Spring Boot Actuator.

Prometheus scrapes:

```text
http://card-service:8080/actuator/prometheus
```

Grafana uses Prometheus as its data source.

## Planned Improvements

* Persistent HTTP idempotency
* Idempotent Kafka consumers
* API rate limiting
* Resilience4j circuit breaker
* Retry, timeout, and bulkhead policies
* Saga orchestration
* Compensating transactions
* Integration tests with Testcontainers
* Load testing
* GitHub Actions CI/CD
* AWS or Azure deployment
* Synthetic transaction generator
* AI-driven failure and transaction scenarios

## Security

Secrets and environment-specific credentials must not be committed to the repository. Local values should be stored in `.env`, while `.env.example` documents the required variables.
