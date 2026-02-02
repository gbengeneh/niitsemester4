@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   Microservices Docker Setup Script
echo ========================================
echo.

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not running. Please start Docker Desktop and try again.
    exit /b 1
)
echo [OK] Docker is running
echo.

REM Create network
echo [INFO] Creating microservices network...
docker network inspect microservices-network >nul 2>&1
if errorlevel 1 (
    docker network create microservices-network
    echo [OK] Network 'microservices-network' created
) else (
    echo [OK] Network 'microservices-network' already exists
)
echo.

REM Start infrastructure services
echo [INFO] Starting infrastructure services...
echo    - Config Server (8888)
echo    - Eureka Server (8761)
echo    - API Gateway (8080)
echo.

docker-compose up --build -d
if errorlevel 1 (
    echo [ERROR] Failed to start infrastructure services
    exit /b 1
)
echo [OK] Infrastructure services started
echo.

REM Wait for services to be healthy
echo [INFO] Waiting for services to be healthy (60 seconds)...
timeout /t 60 /nobreak >nul
echo.

REM Start Customer API
echo [INFO] Starting Customer API...
cd customer_api
docker-compose up --build -d
if errorlevel 1 (
    echo [ERROR] Failed to start Customer API
    cd ..
    exit /b 1
)
echo [OK] Customer API started
cd ..
echo.

REM Start Banking API
echo [INFO] Starting Banking API...
cd bankingapi
docker-compose up --build -d
if errorlevel 1 (
    echo [ERROR] Failed to start Banking API
    cd ..
    exit /b 1
)
echo [OK] Banking API started
cd ..
echo.

REM Wait for services to register
echo [INFO] Waiting for services to register with Eureka (30 seconds)...
timeout /t 30 /nobreak >nul
echo.

echo ========================================
echo   All services started successfully!
echo ========================================
echo.

echo Service URLs:
echo.
echo Infrastructure Services:
echo   Config Server:  http://localhost:8888/actuator/health
echo   Eureka Server:  http://localhost:8761
echo   API Gateway:    http://localhost:8080/actuator/health
echo.
echo Business Services (via Gateway):
echo   Customer API:   http://localhost:8080/customer-api/actuator/health
echo   Banking API:    http://localhost:8080/banking-api/actuator/health
echo.
echo Business Services (Direct):
echo   Customer API:   http://localhost:7074/actuator/health
echo   Banking API:    http://localhost:7075/actuator/health
echo.
echo Monitoring:
echo   Zipkin:         http://localhost:9411
echo.

echo ========================================
echo Useful Commands:
echo ========================================
echo.
echo View logs:
echo   docker-compose logs -f
echo   cd customer_api ^&^& docker-compose logs -f
echo   cd bankingapi ^&^& docker-compose logs -f
echo.
echo Stop all services:
echo   stop-all-services.bat
echo.
echo Check service status:
echo   docker-compose ps
echo.

echo ========================================
echo Setup complete! Check Eureka Dashboard to verify service registration.
echo ========================================
echo.

pause
