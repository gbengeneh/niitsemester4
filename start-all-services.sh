#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Microservices Docker Setup Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker Desktop and try again.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker is running${NC}"
}

# Function to create network
create_network() {
    echo -e "${YELLOW}📡 Creating microservices network...${NC}"
    if docker network inspect microservices-network > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Network 'microservices-network' already exists${NC}"
    else
        docker network create microservices-network
        echo -e "${GREEN}✅ Network 'microservices-network' created${NC}"
    fi
    echo ""
}

# Function to start infrastructure services
start_infrastructure() {
    echo -e "${YELLOW}🏗️  Starting infrastructure services...${NC}"
    echo -e "${BLUE}   - Config Server (8888)${NC}"
    echo -e "${BLUE}   - Eureka Server (8761)${NC}"
    echo -e "${BLUE}   - API Gateway (8080)${NC}"
    echo ""
    
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Infrastructure services started${NC}"
    else
        echo -e "${RED}❌ Failed to start infrastructure services${NC}"
        exit 1
    fi
    echo ""
}

# Function to wait for service health
wait_for_service() {
    local service_name=$1
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}⏳ Waiting for ${service_name} to be healthy...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if docker inspect --format='{{.State.Health.Status}}' $service_name 2>/dev/null | grep -q "healthy"; then
            echo -e "${GREEN}✅ ${service_name} is healthy${NC}"
            return 0
        fi
        echo -e "   Attempt $attempt/$max_attempts..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}❌ ${service_name} failed to become healthy${NC}"
    return 1
}

# Function to start customer API
start_customer_api() {
    echo -e "${YELLOW}👥 Starting Customer API...${NC}"
    cd customer_api
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Customer API started${NC}"
    else
        echo -e "${RED}❌ Failed to start Customer API${NC}"
        cd ..
        return 1
    fi
    cd ..
    echo ""
}

# Function to start banking API
start_banking_api() {
    echo -e "${YELLOW}🏦 Starting Banking API...${NC}"
    cd bankingapi
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Banking API started${NC}"
    else
        echo -e "${RED}❌ Failed to start Banking API${NC}"
        cd ..
        return 1
    fi
    cd ..
    echo ""
}

# Function to display service URLs
display_urls() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Service URLs${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${GREEN}Infrastructure Services:${NC}"
    echo -e "  Config Server:  ${BLUE}http://localhost:8888/actuator/health${NC}"
    echo -e "  Eureka Server:  ${BLUE}http://localhost:8761${NC}"
    echo -e "  API Gateway:    ${BLUE}http://localhost:8080/actuator/health${NC}"
    echo ""
    echo -e "${GREEN}Business Services (via Gateway):${NC}"
    echo -e "  Customer API:   ${BLUE}http://localhost:8080/customer-api/actuator/health${NC}"
    echo -e "  Banking API:    ${BLUE}http://localhost:8080/banking-api/actuator/health${NC}"
    echo ""
    echo -e "${GREEN}Business Services (Direct):${NC}"
    echo -e "  Customer API:   ${BLUE}http://localhost:7074/actuator/health${NC}"
    echo -e "  Banking API:    ${BLUE}http://localhost:7075/actuator/health${NC}"
    echo ""
    echo -e "${GREEN}Monitoring:${NC}"
    echo -e "  Zipkin:         ${BLUE}http://localhost:9411${NC}"
    echo ""
}

# Function to show logs command
show_logs_info() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Useful Commands${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${YELLOW}View logs:${NC}"
    echo -e "  docker-compose logs -f                    # Infrastructure logs"
    echo -e "  cd customer_api && docker-compose logs -f # Customer API logs"
    echo -e "  cd bankingapi && docker-compose logs -f   # Banking API logs"
    echo ""
    echo -e "${YELLOW}Stop all services:${NC}"
    echo -e "  ./stop-all-services.sh"
    echo ""
    echo -e "${YELLOW}Check service status:${NC}"
    echo -e "  docker-compose ps"
    echo -e "  cd customer_api && docker-compose ps"
    echo -e "  cd bankingapi && docker-compose ps"
    echo ""
}

# Main execution
main() {
    check_docker
    create_network
    start_infrastructure
    
    # Wait for infrastructure services
    wait_for_service "config-server"
    wait_for_service "eureka-server"
    wait_for_service "api-gateway"
    
    echo -e "${GREEN}✅ All infrastructure services are healthy${NC}"
    echo ""
    
    # Start business services
    start_customer_api
    start_banking_api
    
    # Give services time to register with Eureka
    echo -e "${YELLOW}⏳ Waiting for services to register with Eureka (30 seconds)...${NC}"
    sleep 30
    
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  ✅ All services started successfully!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    
    display_urls
    show_logs_info
    
    echo -e "${BLUE}========================================${NC}"
    echo -e "${GREEN}🎉 Setup complete! Check Eureka Dashboard to verify service registration.${NC}"
    echo -e "${BLUE}========================================${NC}"
}

# Run main function
main
