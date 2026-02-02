#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Stopping All Microservices${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to stop banking API
stop_banking_api() {
    echo -e "${YELLOW}🏦 Stopping Banking API...${NC}"
    cd bankingapi
    docker-compose down
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Banking API stopped${NC}"
    else
        echo -e "${RED}❌ Failed to stop Banking API${NC}"
    fi
    cd ..
    echo ""
}

# Function to stop customer API
stop_customer_api() {
    echo -e "${YELLOW}👥 Stopping Customer API...${NC}"
    cd customer_api
    docker-compose down
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Customer API stopped${NC}"
    else
        echo -e "${RED}❌ Failed to stop Customer API${NC}"
    fi
    cd ..
    echo ""
}

# Function to stop infrastructure services
stop_infrastructure() {
    echo -e "${YELLOW}🏗️  Stopping infrastructure services...${NC}"
    docker-compose down
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Infrastructure services stopped${NC}"
    else
        echo -e "${RED}❌ Failed to stop infrastructure services${NC}"
    fi
    echo ""
}

# Function to remove network (optional)
remove_network() {
    read -p "Do you want to remove the microservices network? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}📡 Removing microservices network...${NC}"
        docker network rm microservices-network 2>/dev/null
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ Network removed${NC}"
        else
            echo -e "${YELLOW}⚠️  Network may still be in use or already removed${NC}"
        fi
    else
        echo -e "${BLUE}ℹ️  Network kept for future use${NC}"
    fi
    echo ""
}

# Function to clean volumes (optional)
clean_volumes() {
    read -p "Do you want to remove all volumes (databases will be deleted)? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${RED}⚠️  Removing all volumes...${NC}"
        
        # Stop and remove with volumes
        cd bankingapi && docker-compose down -v && cd ..
        cd customer_api && docker-compose down -v && cd ..
        docker-compose down -v
        
        echo -e "${GREEN}✅ All volumes removed${NC}"
    else
        echo -e "${BLUE}ℹ️  Volumes kept (databases preserved)${NC}"
    fi
    echo ""
}

# Main execution
main() {
    # Stop services in reverse order
    stop_banking_api
    stop_customer_api
    stop_infrastructure
    
    # Optional cleanup
    remove_network
    clean_volumes
    
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  ✅ All services stopped${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    
    echo -e "${BLUE}To start services again, run:${NC}"
    echo -e "  ./start-all-services.sh"
    echo ""
}

# Run main function
main
