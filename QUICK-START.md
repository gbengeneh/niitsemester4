# Quick Start Guide

## 🚀 Getting Started in 3 Steps

### Prerequisites
- ✅ Docker Desktop installed and running
- ✅ At least 4GB RAM available for Docker
- ✅ Ports available: 8080, 8761, 8888, 7074, 7075, 5432, 3306, 9411

### Option 1: Using Scripts (Recommended)

#### Windows:
```cmd
start-all-services.bat
```

#### Linux/Mac:
```bash
chmod +x start-all-services.sh
./start-all-services.sh
```

### Option 2: Manual Setup

#### Step 1: Create Network
```bash
docker network create microservices-network
```

#### Step 2: Start Infrastructure
```bash
docker-compose up --build -d
```

#### Step 3: Start Business Services
```bash
# Terminal 1 - Customer API
cd customer_api
docker-compose up --build -d

# Terminal 2 - Banking API
cd bankingapi
docker-compose up --build -d
```

## 🔍 Verify Setup

### Check Eureka Dashboard
Open: http://localhost:8761

You should see:
- ✅ `CUSTOMER_API` registered
- ✅ `BANKINGAPI` registered

### Test Services

```bash
# Config Server
curl http://localhost:8888/actuator/health

# Eureka Server
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health

# Customer API (via Gateway)
curl http://localhost:8080/customer-api/actuator/health

# Banking API (via Gateway)
curl http://localhost:8080/banking-api/actuator/health
```

## 📊 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Service registry |
| Config Server | http://localhost:8888 | Configuration management |
| API Gateway | http://localhost:8080 | Entry point |
| Customer API | http://localhost:7074 | Customer service |
| Banking API | http://localhost:7075 | Banking service |
| Zipkin | http://localhost:9411 | Distributed tracing |

## 🛑 Stop Services

### Using Scripts

#### Windows:
```cmd
stop-all-services.bat
```

#### Linux/Mac:
```bash
./stop-all-services.sh
```

### Manual Stop
```bash
# Stop business services
cd customer_api && docker-compose down && cd ..
cd bankingapi && docker-compose down && cd ..

# Stop infrastructure
docker-compose down
```

## 📝 View Logs

```bash
# Infrastructure logs
docker-compose logs -f

# Specific service
docker-compose logs -f eureka-server

# Customer API logs
cd customer_api && docker-compose logs -f

# Banking API logs
cd bankingapi && docker-compose logs -f
```

## 🔧 Common Issues

### Port Already in Use
```bash
# Windows - Find process using port
netstat -ano | findstr :8080

# Linux/Mac - Find process using port
lsof -i :8080
```

### Service Not Registering with Eureka
1. Check Eureka is running: http://localhost:8761
2. Wait 30-60 seconds for registration
3. Check service logs for errors

### Docker Out of Memory
- Increase Docker memory in Docker Desktop settings
- Recommended: At least 4GB

### Network Issues
```bash
# Recreate network
docker network rm microservices-network
docker network create microservices-network
```

## 📚 Documentation

- **Complete Setup Guide**: `MICROSERVICES-SETUP-GUIDE.md`
- **Setup Summary**: `COMPLETE-SETUP-SUMMARY.md`
- **Infrastructure Guide**: `README-DOCKER.md`

## 🎯 Architecture

```
Client
  ↓
API Gateway (8080)
  ↓
Eureka Server (8761)
  ↓
┌─────────────┬─────────────┐
│             │             │
Customer API  Banking API   Config Server
(7074)        (7075)        (8888)
│             │
PostgreSQL    MySQL
(5432)        (3306)
```

## ✨ Features

- ✅ Service Discovery (Eureka)
- ✅ API Gateway Routing
- ✅ Centralized Configuration
- ✅ Health Checks
- ✅ Database Integration
- ✅ Distributed Tracing (Zipkin)
- ✅ Load Balancing
- ✅ Docker Containerization

## 🆘 Need Help?

1. Check logs first: `docker-compose logs -f [service-name]`
2. Verify all services are healthy: `docker-compose ps`
3. Review troubleshooting in `MICROSERVICES-SETUP-GUIDE.md`
4. Ensure Docker has sufficient resources

---

**Happy Coding! 🎉**
