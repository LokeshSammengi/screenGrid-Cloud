# 🎬 ScreenGrid Cloud – Microservices Movie Booking Platform

ScreenGrid Cloud is a cloud-native movie ticket booking system built using Spring Boot and Spring Cloud microservices architecture.

This project demonstrates real-world microservices patterns including service discovery, centralized configuration, API Gateway routing, security, and inter-service communication.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Cloud
- Eureka Service Registry
- Spring Cloud Gateway
- Spring Cloud Config
- Spring Boot Admin
- REST APIs
- JWT Authentication
- Maven
- MySQL (per service DB design)

---

## 🧩 Microservices Overview

| Service | Description |
|----------|-------------|
| DiscoveryEurekaServer | Service Registry for dynamic service discovery |
| APIGateway | Entry point for all client requests |
| ConfigServer | Centralized configuration management |
| AdminServer | Monitoring dashboard for microservices |
| Catalog_Service | Manages movies, theatres, screens, shows |
| Booking-Microservice | Handles seat booking and reservations |
| Payment-Service | Handles payment processing |
| User-Service | User management and authentication (JWT) |

---

## 🏗 Architecture Diagram

```mermaid
flowchart TD

Client --> APIGateway

APIGateway --> UserService
APIGateway --> CatalogService
APIGateway --> BookingService
APIGateway --> PaymentService

UserService --> Eureka
CatalogService --> Eureka
BookingService --> Eureka
PaymentService --> Eureka
APIGateway --> Eureka
ConfigServer --> AllServices

AdminServer --> AllServices
🔄 Service Communication

API Gateway handles routing.

Services register with Eureka.

Feign Clients used for inter-service communication.

Config Server provides centralized configuration.

JWT used for securing endpoints.

🔐 Security

User authentication using JWT.

Role-based access control.

Gateway-level request filtering.

🛠 How to Run
1️⃣ Start Services in Order

DiscoveryEurekaServer

ConfigServer

AdminServer

APIGateway

Other Microservices

2️⃣ Access

Eureka Dashboard → http://localhost:8761

API Gateway → http://localhost:8080

Admin Dashboard → http://localhost:9090

📌 Future Enhancements

Kafka Event-Driven Communication

Resilience4j Circuit Breaker

ELK Stack Logging

Docker & Docker Compose

CI/CD Integration

👨‍💻 Author

Sai Lokesh Sammengi
