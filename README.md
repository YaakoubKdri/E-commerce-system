# Distributed E-commerce / Order Management System

This project is a **production-style distributed system** that demonstrates how to build an **event-driven Saga-based architecture** using **Spring Boot, Spring Cloud, Kafka, and Resilience4j**.

It is intentionally designed to reflect **real-world microservice concerns**:
- eventual consistency
- failure handling
- resilience patterns
- Kafka at-least-once delivery
- idempotent consumers
- database-per-service

---

## 1. High-Level Architecture

### Microservices
- **Order Service**
    - Entry point for clients
    - Creates orders in `PENDING` state
    - Publishes `OrderCreatedEvent`
- **Inventory Service**
    - Reserves and releases stock
    - Reacts to `OrderCreatedEvent`
    - Publishes `InventoryReservedEvent`
- **Payment Service**
    - Processes payments (simulated external gateway)
    - Reacts to `InventoryReservedEvent`
    - Publishes `PaymentProcessedEvent` or `PaymentFailedEvent`

### Infrastructure
- **Kafka** – Event backbone
- **Eureka** – Service discovery
- **Spring Cloud Config Server** – Centralized configuration
- **PostgreSQL** – One database per service (Dockerized)
- **Adminer** – DB inspection (dev only)

---

## 2. Saga Pattern (Choreography)

This system uses **Saga Choreography**, not orchestration.

### Happy Path
Order Service  
→ OrderCreatedEvent    
Inventory Service    
→ InventoryReservedEvent  
Payment Service  
→ PaymentProcessedEvent  
Order Service  
→ Order COMPLETED  

### Failure Path (Payment Failure)
PaymentFailedEvent  
→ Inventory releases stock  
→ Order is CANCELED  

No synchronous calls between services.  
All coordination happens via **events**.

---

## 3. Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Cloud 2024.x
- Spring Cloud Stream (Kafka Binder)
- Spring Kafka (listeners)
- Resilience4j
- PostgreSQL
- Docker & Docker Compose

---

