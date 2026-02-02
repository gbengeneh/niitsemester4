# Docker Setup for Microservices

This project contains Docker configurations for three Spring Cloud microservices:
- **Config Server** (Port 8888)
- **Eureka Server** (Port 8761)
- **API Gateway** (Port 8080)

## Prerequisites

- Docker Desktop installed and running
- Docker Compose installed
- At least 2GB of available RAM for containers

## Project Structure

```
semester4/
├── config-server/config-server/
│   ├── Dockerfile
│   └── src/main/resources/
│       ├── application.properties
│       └── application-docker.properties
├── eureka-server/
│   ├── Dockerfile
│   └── src/main/resources/
│       ├── application.properties
│       └── application-docker.properties
├── api-gateway/
│   ├── Dockerfile
│   └── src/main/resources/
│       ├── application.properties
│       └── application-docker.properties
├── docker-compose.yml
└── .dockerignore
```

## Services Overview

### 1. Config Server
- **Port**: 8888
- **Purpose**: Centralized configuration management
- **Health Check**: http://localhost:8888/actuator/health

### 2. Eureka Server
- **Port**: 8761
- **Purpose**: Service discovery and registration
- **Dashboard**: http://localhost:8761
- **Health Check**: http://localhost:8761/actuator/health

### 3. API Gateway
- **Port**: 8080
- **Purpose**: Single entry point for all microservices
- **Health Check**: http://localhost:8080/actuator/health

## Quick Start

### Build and Run All Services

```bash
# Build and start all services
docker-compose up --build

# Run in detached mode (background)
docker-compose up --build -d
```

### Stop All Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### View Logs

```bash
# View logs for all services
docker-compose logs -f

# View logs for specific service
docker-compose logs -f config-server
docker-compose logs -f eureka-server
docker-compose logs -f api-gateway
```

## Individual Service Commands

### Build Individual Service

```bash
# Config Server
docker build -t config-server:latest ./config-server/config-server

# Eureka Server
docker build -t eureka-server:latest ./eureka-server

# API Gateway
docker build -t api-gateway:latest ./api-gateway
```

### Run Individual Service

```bash
# Config Server
docker run -p 8888:8888 --name config-server config-server:latest

# Eureka Server
docker run -p 8761:8761 --name eureka-server eureka-server:latest

# API Gateway
docker run -p 8080:8080 --name api-gateway api-gateway:latest
```

## Service Startup Order

The docker-compose.yml ensures proper startup order:
1. **Config Server** starts first
2. **Eureka Server** starts after Config Server is healthy
3. **API Gateway** starts after Eureka Server is healthy

## Health Checks

All services include health checks that verify:
- Service is running
- Actuator endpoints are accessible
- Service is ready to accept requests

Health check intervals:
- Interval: 30 seconds
- Timeout: 10 seconds
- Retries: 5
- Start period: 60 seconds

## Networking

All services run on a custom bridge network called `microservices-network`, allowing them to communicate using service names as hostnames.

## Environment Variables

### Config Server
- `SPRING_PROFILES_ACTIVE=docker`
- `JAVA_OPTS=-Xmx512m -Xms256m`

### Eureka Server
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/`
- `JAVA_OPTS=-Xmx512m -Xms256m`

### API Gateway
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/`
- `JAVA_OPTS=-Xmx512m -Xms256m`

## Troubleshooting

### Services Not Starting

1. Check if ports are already in use:
   ```bash
   # Windows
   netstat -ano | findstr :8888
   netstat -ano | findstr :8761
   netstat -ano | findstr :8080
   ```

2. Check Docker logs:
   ```bash
   docker-compose logs -f [service-name]
   ```

### Service Registration Issues

1. Verify Eureka Server is running:
   ```bash
   curl http://localhost:8761/actuator/health
   ```

2. Check Eureka Dashboard:
   - Open http://localhost:8761 in browser
   - Verify services are registered

### Memory Issues

If services crash due to memory:
1. Increase Docker Desktop memory allocation
2. Adjust JAVA_OPTS in docker-compose.yml

### Rebuild After Code Changes

```bash
# Rebuild specific service
docker-compose up --build config-server

# Rebuild all services
docker-compose up --build
```

## Accessing Services

- **Config Server**: http://localhost:8888
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Actuator Health Endpoints**:
  - Config Server: http://localhost:8888/actuator/health
  - Eureka Server: http://localhost:8761/actuator/health
  - API Gateway: http://localhost:8080/actuator/health

## Clean Up

```bash
# Remove all containers
docker-compose down

# Remove all containers and images
docker-compose down --rmi all

# Remove all containers, images, and volumes
docker-compose down --rmi all -v

# Prune unused Docker resources
docker system prune -a
```

## Production Considerations

For production deployment, consider:
1. Using external configuration repository for Config Server
2. Enabling Eureka Server self-preservation
3. Adding authentication/authorization
4. Implementing rate limiting in API Gateway
5. Using Docker secrets for sensitive data
6. Setting up proper logging and monitoring
7. Implementing circuit breakers
8. Adding SSL/TLS certificates

## Next Steps

1. Add your microservices to the docker-compose.yml
2. Configure routes in API Gateway
3. Add centralized configuration files to Config Server
4. Implement service-to-service communication
5. Add monitoring and logging solutions (e.g., ELK stack, Prometheus)
