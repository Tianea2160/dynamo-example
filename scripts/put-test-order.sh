#!/bin/bash

echo "Inserting test order with nested items..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --item '{
        "userId": {"S": "USER123"},
        "orderId": {"S": "20240524143000-abcd1234"},
        "orderDate": {"S": "2024-05-24T14:30:00"},
        "status": {"S": "PENDING"},
        "statusIndex": {"S": "PENDING"},
        "orderDateIndex": {"S": "2024-05-24T14:30:00"},
        "totalAmount": {"N": "1499.97"},
        "items": {"L": [
            {"M": {
                "productId": {"S": "PROD-1"},
                "productName": {"S": "iPhone 15"},
                "quantity": {"N": "1"},
                "price": {"N": "999.99"}
            }},
            {"M": {
                "productId": {"S": "PROD-2"},
                "productName": {"S": "AirPods Pro"},
                "quantity": {"N": "2"},
                "price": {"N": "249.99"}
            }}
        ]}
    }'

echo "Retrieving the inserted order..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb get-item \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --key '{
        "userId": {"S": "USER123"},
        "orderId": {"S": "20240524143000-abcd1234"}
    }' 