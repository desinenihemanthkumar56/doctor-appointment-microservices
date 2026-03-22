# 🏥 Doctor Appointment Microservices

> A production-ready, cloud-native microservices platform for managing doctor appointments — built with **Spring Boot**, **Spring Cloud**, **Apache Kafka**, and **Docker**.

---

## 📌 Overview

This project demonstrates a fully distributed, event-driven backend system for a healthcare appointment management platform. It follows microservices best practices with independent, loosely-coupled services that communicate via REST and asynchronous messaging using Apache Kafka.

The system handles the complete lifecycle of a medical appointment — from patient registration and authentication, to doctor management, booking, and payment processing — all behind a unified API Gateway.

---

## 🏗️ Architecture

```
                        ┌──────────────────┐
                        │   API Gateway    │  ← Single entry point for all clients
                        └────────┬─────────┘
                                 │
           ┌─────────────────────┼──────────────────────┐
           │                     │                      │
    ┌──────▼──────┐       ┌──────▼──────┐       ┌──────▼──────┐
    │ Auth Service│       │Doc  Service │       │Patient Svc  │
    └─────────────┘       └─────────────┘       └─────────────┘
           │                     │                      │
    ┌──────▼──────┐       ┌──────▼──────┐       ┌──────▼──────┐
    │Booking Svc  │       │Payment Svc  │       │Eureka Server│
    └──────┬──────┘       └──────┬──────┘       └─────────────┘
           │                     │
           └──────────┬──────────┘
                      │
              ┌───────▼───────┐
              │  Apache Kafka │  ← Async event bus
              └───────────────┘
```

**Service Discovery** is handled by Netflix Eureka — all services self-register and are discoverable by name, enabling dynamic load balancing.

---

## 🧩 Microservices

| Service | Responsibility |
|---|---|
| **API Gateway** | Routes all incoming requests to the appropriate downstream service. Acts as the single entry point for clients. |
| **Auth Service** | Handles user registration, login, and JWT-based authentication. Secures all protected endpoints. |
| **Doctor Service** | Manages doctor profiles, specializations, and availability slots. |
| **Patient Service** | Manages patient records, profiles, and medical history data. |
| **Booking Service** | Orchestrates appointment creation, cancellation, rescheduling, and status tracking. |
| **Payment Service** | Processes appointment payments and listens to booking events via Kafka. |
| **Eureka Server** | Service registry for dynamic discovery and registration of all microservices. |

---

## ⚙️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java |
| **Framework** | Spring Boot, Spring Cloud |
| **Service Discovery** | Netflix Eureka |
| **API Gateway** | Spring Cloud Gateway |
| **Messaging** | Apache Kafka + Zookeeper |
| **Authentication** | JWT (JSON Web Tokens) |
| **Containerization** | Docker, Docker Compose |
| **Build Tool** | Maven |

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. Clone the Repository

```bash
git clone https://github.com/desinenihemanthkumar56/doctor-appointment-microservices.git
cd doctor-appointment-microservices
```

### 2. Start Infrastructure (Kafka + Zookeeper)

```bash
docker-compose up -d
```

This spins up:
- **Zookeeper** on port `2181`
- **Kafka Broker** on port `9092`

### 3. Start Services (in order)

Start each service by navigating into its directory and running:

```bash
# 1. Service Registry
cd eureka-server && mvn spring-boot:run

# 2. API Gateway
cd API-Gateway && mvn spring-boot:run

# 3. Auth Service
cd auth-service && mvn spring-boot:run

# 4. Doctor Service
cd doctor && mvn spring-boot:run

# 5. Patient Service
cd patient-service && mvn spring-boot:run

# 6. Booking Service
cd booking-service && mvn spring-boot:run

# 7. Payment Service
cd payment-service && mvn spring-boot:run
```

### 4. Verify Services Are Running

Once all services are up, visit the Eureka dashboard at:

```
http://localhost:8761
```

All registered services should appear as **UP**.

---

## 📡 API Overview

All requests are routed through the **API Gateway**. Authenticate first to receive a JWT token, then include it in the `Authorization: Bearer <token>` header for protected routes.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Login and receive JWT |
| `GET` | `/doctors` | List all doctors |
| `GET` | `/doctors/{id}` | Get doctor details |
| `GET` | `/patients/{id}` | Get patient profile |
| `POST` | `/bookings` | Book an appointment |
| `GET` | `/bookings/{id}` | View booking details |
| `DELETE` | `/bookings/{id}` | Cancel a booking |
| `POST` | `/payments` | Process payment for a booking |

---

## 🔄 Event-Driven Flow

The **Booking Service** and **Payment Service** communicate asynchronously via Kafka:

1. Patient books an appointment → Booking Service publishes a `booking-created` event to Kafka
2. Payment Service consumes the event → triggers payment processing
3. Payment Service publishes a `payment-confirmed` event
4. Booking Service updates appointment status to `CONFIRMED`

This decoupled approach ensures reliability and resilience — if the Payment Service is temporarily down, no booking events are lost.

---

## 📁 Project Structure

```
doctor-appointment-microservices/
│
├── API-Gateway/           # Spring Cloud Gateway
├── auth-service/          # JWT Authentication
├── booking-service/       # Appointment management
├── doctor/                # Doctor profiles & availability
├── eureka-server/         # Service registry (Netflix Eureka)
├── patient-service/       # Patient management
├── payment-service/       # Payment processing
└── docker-compose.yml     # Kafka + Zookeeper setup
```

---

## 🔮 Future Enhancements

- [ ] Add Zipkin/Sleuth for distributed tracing
- [ ] Integrate Spring Cloud Config Server for centralized config management
- [ ] Add email/SMS notifications via Kafka consumer
- [ ] Implement circuit breaker pattern with Resilience4j
- [ ] Dockerize all microservices for full container orchestration
- [ ] Add Kubernetes deployment manifests

---

## 👨‍💻 Author

**Hemanth Kumar Desinehi**
- GitHub: [@desinenihemanthkumar56](https://github.com/desinenihemanthkumar56)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
