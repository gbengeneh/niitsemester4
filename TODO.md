# Docker Setup for Microservices - TODO List

## 1. Config Server Setup
- [x] Add @EnableConfigServer annotation to ConfigServerApplication.java
- [x] Update application.properties with proper configuration
- [x] Create Dockerfile for config-server
- [x] Create application-docker.properties

## 2. Eureka Server Setup
- [x] Add @EnableEurekaServer annotation to EurekaServerApplication.java
- [x] Update application.properties with proper Eureka configuration
- [x] Create Dockerfile for eureka-server
- [x] Create application-docker.properties

## 3. API Gateway Setup
- [x] Add @EnableDiscoveryClient annotation to ApiGatewayApplication.java
- [x] Update application.properties with gateway and Eureka client configuration
- [x] Create Dockerfile for api-gateway
- [x] Create application-docker.properties

## 4. Infrastructure Docker Compose
- [x] Create root-level docker-compose.yml
- [x] Configure service dependencies and networking
- [x] Add health checks

## 5. Customer API Setup
- [x] Add Spring Cloud and Eureka Client dependencies to pom.xml
- [x] Add @EnableDiscoveryClient annotation
- [x] Update application.properties with Eureka configuration
- [x] Create application-docker.properties
- [x] Update/Create Dockerfile
- [x] Update docker-compose.yml with PostgreSQL

## 6. Banking API Setup
- [x] Add Eureka Client dependency to pom.xml
- [x] Add @EnableDiscoveryClient annotation
- [x] Update application.properties with Eureka configuration (changed port to 7075)
- [x] Create application-docker.properties
- [x] Update existing Dockerfile
- [x] Update docker-compose.yml with MySQL

## 7. API Gateway Routes
- [x] Add routes for customer-api
- [x] Add routes for banking-api

## 7. Testing (Next Steps)
- [ ] Build infrastructure services (config, eureka, gateway)
- [ ] Build customer_api and bankingapi
- [ ] Verify both services register with Eureka
- [ ] Test API Gateway routing to both services

## Commands to Run:

### Infrastructure Services:
```bash
# Start config-server, eureka-server, api-gateway
docker-compose up --build
```

### Customer API:
```bash
cd customer_api
docker-compose up --build
```

### Banking API:
```bash
cd bankingapi
docker-compose up --build
```

### Access Services:
- Config Server: http://localhost:8888/actuator/health
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080/actuator/health
- Customer API: http://localhost:7074 (via Gateway: http://localhost:8080/customer_api)
- Banking API: http://localhost:7075 (via Gateway: http://localhost:8080/bankingapi)
