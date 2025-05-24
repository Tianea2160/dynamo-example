#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/orders"

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m'

# Create a test user first
USER_ID=$(curl -s -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d '{
        "name": "John Doe",
        "email": "john.doe@example.com",
        "age": 30
    }' | jq -r '.id')

echo -e "${GREEN}1. Creating a new order${NC}"
ORDER_ID=$(curl -s -X POST $BASE_URL \
    -H "Content-Type: application/json" \
    -d '{
        "userId": "'$USER_ID'",
        "items": [
            {
                "productId": "PROD-1",
                "productName": "iPhone 15",
                "quantity": 1,
                "price": 999.99
            },
            {
                "productId": "PROD-2",
                "productName": "AirPods Pro",
                "quantity": 2,
                "price": 249.99
            }
        ]
    }' | jq -r '.orderId')
echo "Created order with ID: $ORDER_ID"
echo

echo -e "${GREEN}2. Getting orders by user ID${NC}"
curl -s -X GET "$BASE_URL/user/$USER_ID" | jq
echo

echo -e "${GREEN}3. Getting orders by status (PENDING)${NC}"
curl -s -X GET "$BASE_URL/status/PENDING" | jq
echo

echo -e "${GREEN}4. Getting orders by status and date range${NC}"
START_DATE=$(date -u +"%Y-%m-%dT%H:%M:%S")
END_DATE=$(date -u -v+1d +"%Y-%m-%dT%H:%M:%S")
curl -s -X GET "$BASE_URL/status/PENDING/date-range?startDate=$START_DATE&endDate=$END_DATE" | jq
echo

echo -e "${GREEN}5. Getting user orders by date range${NC}"
curl -s -X GET "$BASE_URL/user/$USER_ID/date-range?startDate=$START_DATE&endDate=$END_DATE" | jq
echo

echo -e "${GREEN}6. Updating order status to PAID${NC}"
curl -s -X PUT "$BASE_URL/$USER_ID/$ORDER_ID/status" \
    -H "Content-Type: application/json" \
    -d '{
        "status": "PAID"
    }' | jq
echo

echo -e "${GREEN}7. Verifying updated status${NC}"
curl -s -X GET "$BASE_URL/status/PAID" | jq
echo 