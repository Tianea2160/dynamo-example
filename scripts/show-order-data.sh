#!/bin/bash

echo "Scanning orders table to show actual DynamoDB data structure..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb scan \
    --endpoint-url http://localhost:8000 \
    --table-name orders 