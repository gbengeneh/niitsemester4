# Microservices Setup Guide

This guide explains how to run the complete microservices architecture with Docker.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway (8080)                       │
│              Routes: /customer-api/**, /banking-api/**       │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                  Eureka Server (8761)                        │
│                   Service Discovery                          │
└────────────────────────┬────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┐
         ▼               ▼               ▼
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ Config      │  │ Customer    │  │ Banking     │
│ Server      │  │ API (7074)  │  │ API (7075)  │
│ (8888)      │  │             │  │             │
└─────────────┘  └──────┬──────┘  └──────┬──────┘
                        │                │
                        ▼                ▼
                 ┌─────────────┐  ┌─────────────┐
                 │ PostgreSQL  │  │   MySQL     │
                 │   (5432)    │  │   (3306)    │
                 └─────────────┘  └─────────────┘
```

## Services

### Infrastructure Services
1. **Config Server** (Port 8888) - Centralized configuration management
2. **Eureka Server** (Port 8761) - Service discovery and registration
3. **API Gateway** (Port 8080) - Single entry point for all microservices

### Business Services
4. **Customer API** (Port 7074) - Customer management service with PostgreSQL
5. **Banking API** (Port 7075) - Banking operations service with MySQL

## Prerequisites

- Docker Desktop installed and running
- Docker Compose installed
- At least 4GB of RAM available for Docker
- Ports 8080, 8761, 8888, 7074, 7075, 5432, 3306 available

## Setup Instructions

### Step 1: Create the Shared Network

First, create the shared Docker network that all services will use:

```bash
docker network create microservices-network
```

### Step 2: Start Infrastructure Services

Start the core infrastructure services (Config Server, Eureka Server, API Gateway):

```bash
# From the root directory
docker-compose up --build -d
```

Wait for all services to be healthy. You can check the status with:

```bash
docker-compose ps
```

### Step 3: Start Customer API

In a new terminal, navigate to the customer_api directory and start the service:

```bash
cd customer_api
docker-compose up --build -d
```

### Step 4: Start Banking API

In another terminal, navigate to the bankingapi directory and start the service:

```bash
cd bankingapi
docker-compose up --build -d
```

## Verification

### 1. Check All Services are Running

```bash
# Infrastructure services
docker-compose ps

# Customer API
cd customer_api && docker-compose ps

# Banking API
cd bankingapi && docker-compose ps
```

### 2. Access Service Dashboards

- **Eureka Dashboard**: http://localhost:8761
  - You should see `CUSTOMER_API` and `BANKINGAPI` registered
  
- **Config Server**: http://localhost:8888/actuator/health
  
- **API Gateway**: http://localhost:8080/actuator/health

### 3. Test Service Registration

Check if services are registered with Eureka:

```bash
curl http://localhost:8761/eureka/apps
```

### 4. Test API Gateway Routing

#### Customer API through Gateway:
```bash
# Replace with actual customer API endpoint
curl http://localhost:8080/customer-api/actuator/health
```

#### Banking API through Gateway:
```bash
# Replace with actual banking API endpoint
curl http://localhost:8080/banking-api/actuator/health
```

#### Direct Access (for testing):
```bash
# Customer API directly
curl http://localhost:7074/actuator/health

# Banking API directly
curl http://localhost:7075/actuator/health
```

## Service URLs

### Infrastructure
- Config Server: http://localhost:8888
- Eureka Server: http://localhost:8761
- API Gateway: http://localhost:8080

### Business Services (via Gateway)
- Customer API: http://localhost:8080/customer-api/**
- Banking API: http://localhost:8080/banking-api/**

### Business Services (Direct)
- Customer API: http://localhost:7074
- Banking API: http://localhost:7075

### Databases
- PostgreSQL (Customer API): localhost:5432
  - Database: niitdevcustomerdb
  - Username: postgres
  - Password: 123

- MySQL (Banking API): localhost:3306
  - Database: customerdb
  - Username: root
  - Password: password

## Viewing Logs

### Infrastructure Services
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f eureka-server
docker-compose logs -f api-gateway
docker-compose logs -f config-server
```

### Business Services
```bash
# Customer API
cd customer_api
docker-compose logs -f customer-api

# Banking API
cd bankingapi
docker-compose logs -f bankingapi
```

## Stopping Services

### Stop All Services
```bash
# Infrastructure services
docker-compose down

# Customer API
cd customer_api && docker-compose down

# Banking API
cd bankingapi && docker-compose down
```

### Stop and Remove Volumes (Clean Slate)
```bash
# Infrastructure
docker-compose down -v

# Customer API
cd customer_api && docker-compose down -v

# Banking API
cd bankingapi && docker-compose down -v
```

## Troubleshooting

### Services Not Registering with Eureka

1. Check if Eureka Server is running:
   ```bash
   curl http://localhost:8761/actuator/health
   ```

2. Check service logs for connection errors:
   ```bash
   docker-compose logs -f [service-name]
   ```

3. Verify network connectivity:
   ```bash
   docker network inspect microservices-network
   ```

### Database Connection Issues

1. Check if database containers are running:
   ```bash
   docker ps | grep postgres
   docker ps | grep mysql
   ```

2. Verify database health:
   ```bash
   # PostgreSQL
   docker exec -it customer-postgres pg_isready -U postgres
   
   # MySQL
   docker exec -it banking-mysql mysqladmin ping -h localhost -u root -ppassword
   ```

### Port Conflicts

If you get port binding errors, check if ports are already in use:

```bash
# Windows
netstat -ano | findstr :[PORT]

# Linux/Mac
lsof -i :[PORT]
```

### Service Won't Start

1. Check Docker resources (CPU, Memory)
2. Review service logs for errors
3. Ensure all dependencies are healthy before starting dependent services

## Development Workflow

### Making Changes to a Service

1. Stop the service:
   ```bash
   docker-compose stop [service-name]
   ```

2. Make your code changes

3. Rebuild and restart:
   ```bash
   docker-compose up --build -d [service-name]
   ```

### Rebuilding Everything

```bash
# Infrastructure
docker-compose down
docker-compose build --no-cache
docker-compose up -d

# Repeat for customer_api and bankingapi
```

## Health Checks

All services include health checks. You can monitor them:

```bash
# Check health status
docker inspect --format='{{.State.Health.Status}}' [container-name]

# View health check logs
docker inspect --format='{{range .State.Health.Log}}{{.Output}}{{end}}' [container-name]
```

## Network Architecture

All services communicate through the `microservices-network` Docker network:

- Infrastructure services use service names for communication (e.g., `eureka-server:8761`)
- Business services use `host.docker.internal` to reach infrastructure services
- This allows business services to run in separate compose files while still communicating

## Best Practices

1. **Always start infrastructure services first** (config-server → eureka-server → api-gateway)
2. **Wait for health checks** before starting dependent services
3. **Use the API Gateway** for external access to business services
4. **Monitor Eureka Dashboard** to ensure all services are registered
5. **Check logs regularly** for any errors or warnings

## Additional Resources

- Spring Cloud Config: https://spring.io/projects/spring-cloud-config
- Netflix Eureka: https://spring.io/projects/spring-cloud-netflix
- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Docker Compose: https://docs.docker.com/compose/

## Support

For issues or questions:
1. Check the logs first
2. Verify all prerequisites are met
3. Ensure Docker has sufficient resources
4. Review the troubleshooting section
