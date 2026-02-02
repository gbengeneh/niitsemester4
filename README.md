 # Microservices Architecture with Docker

A complete Spring Boot microservices architecture with service discovery, API gateway, and centralized configuration management.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway (8080)                       │
│              Single Entry Point for All Services             │
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
│ Server      │  │ API         │  │ API         │
│ (8888)      │  │ (7074)      │  │ (7075)      │
└─────────────┘  └──────┬──────┘  └──────┬──────┘
                        │                │
                        ▼                ▼
                 ┌─────────────┐  ┌─────────────┐
                 │ PostgreSQL  │  │   MySQL     │
                 │   (5432)    │  │   (3306)    │
                 └─────────────┘  └─────────────┘
```

## 📦 Services

### Infrastructure Services
1. **Config Server** (Port 8888) - Centralized configuration management
2. **Eureka Server** (Port 8761) - Service discovery and registration
3. **API Gateway** (Port 8080) - Single entry point with routing

### Business Services
4. **Customer API** (Port 7074) - Customer management with PostgreSQL
5. **Banking API** (Port 7075) - Banking operations with MySQL

## 🚀 Quick Start

### Prerequisites
- Docker Desktop installed and running
- At least 4GB RAM available for Docker
- Ports available: 8080, 8761, 8888, 7074, 7075, 5432, 3306

### Start All Services

#### Windows:
```cmd
start-all-services.bat
```

#### Linux/Mac:
```bash
chmod +x start-all-services.sh
./start-all-services.sh
```

### Verify Setup
Open Eureka Dashboard: http://localhost:8761

You should see `CUSTOMER_API` and `BANKINGAPI` registered.

## 📚 Documentation

- **[Quick Start Guide](QUICK-START.md)** - Get up and running in minutes
- **[Complete Setup Guide](MICROSERVICES-SETUP-GUIDE.md)** - Detailed setup instructions
- **[Setup Summary](COMPLETE-SETUP-SUMMARY.md)** - What has been configured
- **[Infrastructure Guide](README-DOCKER.md)** - Infrastructure services details

## 🔗 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | View registered services |
| Config Server | http://localhost:8888/actuator/health | Configuration server |
| API Gateway | http://localhost:8080/actuator/health | Gateway health |
| Customer API (Gateway) | http://localhost:8080/customer-api/** | Via gateway |
| Banking API (Gateway) | http://localhost:8080/banking-api/** | Via gateway |
| Customer API (Direct) | http://localhost:7074 | Direct access |
| Banking API (Direct) | http://localhost:7075 | Direct access |
| Zipkin | http://localhost:9411 | Distributed tracing |

## 🛠️ Technology Stack

- **Spring Boot** 3.4.1 / 3.3.4 / 3.5.10
- **Spring Cloud** 2024.0.0 / 2025.0.1
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Config** - Configuration Management
- **PostgreSQL** 16 - Customer API Database
- **MySQL** 8.0 - Banking API Database
- **Docker** & **Docker Compose** - Containerization
- **Zipkin** - Distributed Tracing

## 📋 Features

- ✅ Service Discovery with Eureka
- ✅ API Gateway with dynamic routing
- ✅ Centralized configuration management
- ✅ Load balancing
- ✅ Health checks for all services
- ✅ Database integration (PostgreSQL & MySQL)
- ✅ Distributed tracing with Zipkin
- ✅ Docker containerization
- ✅ Multi-stage Docker builds
- ✅ Separate development and production configs

## 🔧 Development

### View Logs
```bash
# Infrastructure services
docker-compose logs -f

# Customer API
cd customer_api && docker-compose logs -f

# Banking API
cd bankingapi && docker-compose logs -f
```

### Stop Services
```bash
# Windows
stop-all-services.bat

# Linux/Mac
./stop-all-services.sh
```

### Rebuild a Service
```bash
# Stop the service
docker-compose stop [service-name]

# Rebuild and restart
docker-compose up --build -d [service-name]
```

## 🧪 Testing

### Test Service Registration
```bash
curl http://localhost:8761/eureka/apps
```

### Test API Gateway Routing
```bash
# Customer API via Gateway
curl http://localhost:8080/customer-api/actuator/health

# Banking API via Gateway
curl http://localhost:8080/banking-api/actuator/health
```

### Test Direct Access
```bash
# Customer API
curl http://localhost:7074/actuator/health

# Banking API
curl http://localhost:7075/actuator/health
```

## 📊 Project Structure

```
semester4/
├── config-server/          # Configuration server
│   └── config-server/
│       ├── src/
│       ├── Dockerfile
│       └── pom.xml
├── eureka-server/          # Service discovery
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── api-gateway/            # API Gateway
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── customer_api/           # Customer service
│   ├── src/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
├── bankingapi/             # Banking service
│   ├── src/
│   ├── dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
├── docker-compose.yml      # Infrastructure orchestration
├── start-all-services.sh   # Start script (Linux/Mac)
├── start-all-services.bat  # Start script (Windows)
├── stop-all-services.sh    # Stop script (Linux/Mac)
├── stop-all-services.bat   # Stop script (Windows)
└── README.md               # This file
```

## 🐛 Troubleshooting

### Services Not Registering
1. Check Eureka is running: http://localhost:8761
2. Wait 30-60 seconds for registration
3. Check service logs: `docker-compose logs -f [service-name]`

### Port Conflicts
```bash
# Windows
netstat -ano | findstr :[PORT]

# Linux/Mac
lsof -i :[PORT]
```

### Database Connection Issues
```bash
# Check PostgreSQL
docker exec -it customer-postgres pg_isready -U postgres

# Check MySQL
docker exec -it banking-mysql mysqladmin ping -h localhost -u root -ppassword
```

### Docker Out of Memory
- Increase Docker memory in Docker Desktop settings
- Recommended: At least 4GB RAM

## 🔐 Database Credentials

### PostgreSQL (Customer API)
- **Host**: localhost:5432
- **Database**: niitdevcustomerdb
- **Username**: postgres
- **Password**: 123

### MySQL (Banking API)
- **Host**: localhost:3306
- **Database**: customerdb
- **Username**: root
- **Password**: password

## 🚦 Service Startup Order

1. **Config Server** starts first
2. **Eureka Server** waits for Config Server
3. **API Gateway** waits for Eureka Server
4. **Business Services** register with Eureka

## 📈 Monitoring

- **Eureka Dashboard**: http://localhost:8761 - View all registered services
- **Actuator Endpoints**: `/actuator/health` - Health status of each service
- **Zipkin**: http://localhost:9411 - Distributed tracing (Banking API)

## 🤝 Contributing

1. Make changes to the service
2. Test locally
3. Rebuild Docker image: `docker-compose up --build -d [service-name]`
4. Verify in Eureka Dashboard

## 📝 License

This project is for educational purposes.

## 🆘 Support

For detailed troubleshooting and setup instructions, see:
- [MICROSERVICES-SETUP-GUIDE.md](MICROSERVICES-SETUP-GUIDE.md)
- [QUICK-START.md](QUICK-START.md)

---

**Built with ❤️ using Spring Boot and Docker**
