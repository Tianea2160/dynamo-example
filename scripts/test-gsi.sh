#!/bin/bash

echo "1. Inserting multiple orders with different statuses..."
# First order (PENDING)
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --item '{
        "userId": {"S": "USER123"},
        "orderId": {"S": "20240524143000-order1"},
        "orderDate": {"S": "2024-05-24T14:30:00"},
        "status": {"S": "PENDING"},
        "statusIndex": {"S": "PENDING"},
        "orderDateIndex": {"S": "2024-05-24T14:30:00"},
        "totalAmount": {"N": "1499.97"},
        "items": {"L": [{"M": {"productId": {"S": "PROD-1"}}}]}
    }'

# Second order (PAID)
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --item '{
        "userId": {"S": "USER456"},
        "orderId": {"S": "20240524143100-order2"},
        "orderDate": {"S": "2024-05-24T14:31:00"},
        "status": {"S": "PAID"},
        "statusIndex": {"S": "PAID"},
        "orderDateIndex": {"S": "2024-05-24T14:31:00"},
        "totalAmount": {"N": "999.99"},
        "items": {"L": [{"M": {"productId": {"S": "PROD-2"}}}]}
    }'

# Third order (PENDING)
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --item '{
        "userId": {"S": "USER789"},
        "orderId": {"S": "20240524143200-order3"},
        "orderDate": {"S": "2024-05-24T14:32:00"},
        "status": {"S": "PENDING"},
        "statusIndex": {"S": "PENDING"},
        "orderDateIndex": {"S": "2024-05-24T14:32:00"},
        "totalAmount": {"N": "299.99"},
        "items": {"L": [{"M": {"productId": {"S": "PROD-3"}}}]}
    }'

echo "2. Query by primary key (specific user's orders)..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb query \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --key-condition-expression "userId = :userId" \
    --expression-attribute-values '{":userId": {"S": "USER123"}}'

echo "3. Query using GSI (all PENDING orders)..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb query \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --index-name status-index \
    --key-condition-expression "statusIndex = :status" \
    --expression-attribute-values '{":status": {"S": "PENDING"}}'

echo "4. Query using GSI with date range (PENDING orders in time range)..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb query \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --index-name status-index \
    --key-condition-expression "statusIndex = :status AND orderDateIndex BETWEEN :start AND :end" \
    --expression-attribute-values '{
        ":status": {"S": "PENDING"},
        ":start": {"S": "2024-05-24T14:00:00"},
        ":end": {"S": "2024-05-24T15:00:00"}
    }' 