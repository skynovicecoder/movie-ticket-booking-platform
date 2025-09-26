# Movie Ticket Booking Platform (MTBP)

A **microservices-based movie ticket booking platform** built using **Spring Boot**, **Java 21**, and **Maven**.  
This project follows a **monorepo structure** with shared modules for DTOs and utilities, allowing easy local development, integration, and dependency management.

---

## 🏗 Project Structure

movie-ticket-booking-platform/
├── mtbp-company-bom/ # Company BOM for dependency versions
├── mtbp-shared-dto/ # Shared DTOs / models
├── mtbp-shared-utils/ # Shared utility classes
├── customer-service/ # Customer Service microservice
├── booking-service/ # Booking Service microservice
├── payment-service/ # Payment Service microservice
├── inventory-service/ # Inventory Management Service microservice
└── theatre-partner-service/ # Theatre Partner Service microservice

---

## 📦 Prerequisites

- Java 21
- Maven 3.8+
- Git
- Optional: Docker (for containerized development)

---

## ⚙️ Setup & Build

1. Clone the repository:

```bash
git clone https://github.com/yourusername/movie-ticket-booking-platform.git
cd movie-ticket-booking-platform
```
2. Build the project using Maven:
mvn clean install
This builds the shared modules first (mtbp-shared-dto, mtbp-shared-utils) and then all microservices.

🚀 Running Services Locally

Each service runs on a different port:

Service	                        Port
Customer Service	            8081
Booking Service	                8082
Payment Service	                8083
Inventory Service	            8084
Theatre Partner Service	        8085

Option 1: Run services individually
cd customer-service
mvn spring-boot:run

cd booking-service
mvn spring-boot:run

Option 2: Run all services in one go

Create a script in project root named run-all.sh (Linux/Mac):

#!/bin/bash
echo "Starting all services..."

mvn -pl customer-service spring-boot:run &
mvn -pl booking-service spring-boot:run &
mvn -pl payment-service spring-boot:run &
mvn -pl inventory-service spring-boot:run &
mvn -pl theatre-partner-service spring-boot:run &

wait
echo "All services started!"

Make it executable:

chmod +x run-all.sh
./run-all.sh


Windows users can create a run-all.bat with similar commands.

🧩 Shared Modules
mtbp-shared-dto

Contains common request/response DTOs shared across services.

Ensures consistent data models across services.

mtbp-shared-utils

Contains utility classes, constants, and helper functions.

Reusable logic for services.

📄 API Documentation

Swagger/OpenAPI endpoints can be added for each microservice.

Example (once service is running):

http://localhost:8081/swagger-ui.html
http://localhost:8082/swagger-ui.html

🛠 Features

Microservices architecture

Centralized dependency management via BOM

Shared DTOs and utilities

Easy local integration of all services

Maven-based build & run

Services can run independently or all together

📝 Notes / Best Practices

Build from root:
mvn clean install


Avoid port conflicts: Each service has a dedicated port in application.yml.
Shared modules are automatically picked up during build; no need to publish for local development.

.gitignore ensures target/, logs, and IDE files are not committed.

🔧 Troubleshooting

Dependency version errors: Make sure your mtbp-company-bom manages versions for shared modules.
Ports in use: Stop other processes or change ports in application.yml.
IntelliJ Maven vs Gradle: Make sure project is imported as Maven, not Gradle.

👤 Author

Sonil Kumar
Email: sonilcs0087@gmail.com

LinkedIn: linkedin.com/in/sonilkumar

🔗 License

MIT License


---

### ✅ Key Features of This README

- Full **monorepo structure explanation**
- Instructions to **build and run all services in one go**
- Shared modules and **dependency info**
- Table for ports for clarity
- Professional format for GitHub

---