#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/users"

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m'

echo -e "${GREEN}1. Creating a new user${NC}"
USER_ID=$(curl -s -X POST $BASE_URL \
    -H "Content-Type: application/json" \
    -d '{
        "name": "John Doe",
        "email": "john.doe@example.com",
        "age": 30
    }' | jq -r '.id')
echo "Created user with ID: $USER_ID"
echo

echo -e "${GREEN}2. Getting user by ID${NC}"
curl -s -X GET "$BASE_URL/$USER_ID" | jq
echo

echo -e "${GREEN}3. Getting user by email${NC}"
curl -s -X GET "$BASE_URL/email/john.doe@example.com" | jq
echo

echo -e "${GREEN}4. Creating another user${NC}"
curl -s -X POST $BASE_URL \
    -H "Content-Type: application/json" \
    -d '{
        "name": "Jane Smith",
        "email": "jane.smith@example.com",
        "age": 25
    }' | jq
echo

echo -e "${GREEN}5. Searching users by name 'John'${NC}"
curl -s -X GET "$BASE_URL/search?name=John" | jq
echo

echo -e "${GREEN}6. Getting all users${NC}"
curl -s -X GET $BASE_URL | jq
echo

echo -e "${GREEN}7. Updating user${NC}"
curl -s -X PUT "$BASE_URL/$USER_ID" \
    -H "Content-Type: application/json" \
    -d '{
        "name": "John Doe Jr",
        "age": 31
    }' | jq
echo

echo -e "${GREEN}8. Deleting user${NC}"
curl -s -X DELETE "$BASE_URL/$USER_ID"
echo "User deleted"
echo

echo -e "${GREEN}9. Verifying deletion${NC}"
curl -s -X GET "$BASE_URL/$USER_ID" | jq
echo 