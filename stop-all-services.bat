@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   Stopping All Microservices
echo ========================================
echo.

REM Stop Banking API
echo [INFO] Stopping Banking API...
cd bankingapi
docker-compose down
if errorlevel 1 (
    echo [ERROR] Failed to stop Banking API
) else (
    echo [OK] Banking API stopped
)
cd ..
echo.

REM Stop Customer API
echo [INFO] Stopping Customer API...
cd customer_api
docker-compose down
if errorlevel 1 (
    echo [ERROR] Failed to stop Customer API
) else (
    echo [OK] Customer API stopped
)
cd ..
echo.

REM Stop Infrastructure services
echo [INFO] Stopping infrastructure services...
docker-compose down
if errorlevel 1 (
    echo [ERROR] Failed to stop infrastructure services
) else (
    echo [OK] Infrastructure services stopped
)
echo.

REM Ask about network removal
set /p remove_network="Do you want to remove the microservices network? (y/N): "
if /i "%remove_network%"=="y" (
    echo [INFO] Removing microservices network...
    docker network rm microservices-network 2>nul
    if errorlevel 1 (
        echo [WARNING] Network may still be in use or already removed
    ) else (
        echo [OK] Network removed
    )
) else (
    echo [INFO] Network kept for future use
)
echo.

REM Ask about volume removal
set /p remove_volumes="Do you want to remove all volumes (databases will be deleted)? (y/N): "
if /i "%remove_volumes%"=="y" (
    echo [WARNING] Removing all volumes...
    
    cd bankingapi
    docker-compose down -v
    cd ..
    
    cd customer_api
    docker-compose down -v
    cd ..
    
    docker-compose down -v
    
    echo [OK] All volumes removed
) else (
    echo [INFO] Volumes kept (databases preserved)
)
echo.

echo ========================================
echo   All services stopped
echo ========================================
echo.

echo To start services again, run:
echo   start-all-services.bat
echo.

pause
