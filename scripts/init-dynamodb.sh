#!/bin/bash

echo "Waiting for DynamoDB Local to start..."
sleep 5

echo "Creating users table..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb create-table \
    --endpoint-url http://localhost:8000 \
    --table-name users \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

echo "Creating orders table..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb create-table \
    --endpoint-url http://localhost:8000 \
    --table-name orders \
    --attribute-definitions \
        AttributeName=userId,AttributeType=S \
        AttributeName=orderId,AttributeType=S \
        AttributeName=statusIndex,AttributeType=S \
        AttributeName=orderDateIndex,AttributeType=S \
    --key-schema \
        AttributeName=userId,KeyType=HASH \
        AttributeName=orderId,KeyType=RANGE \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"status-index\",
                \"KeySchema\": [
                    {\"AttributeName\": \"statusIndex\", \"KeyType\": \"HASH\"},
                    {\"AttributeName\": \"orderDateIndex\", \"KeyType\": \"RANGE\"}
                ],
                \"Projection\": {
                    \"ProjectionType\": \"ALL\"
                },
                \"ProvisionedThroughput\": {
                    \"ReadCapacityUnits\": 5,
                    \"WriteCapacityUnits\": 5
                }
            }
        ]" \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

echo "Verifying table creation..."
docker run --rm --network host \
    -e AWS_ACCESS_KEY_ID=local \
    -e AWS_SECRET_ACCESS_KEY=local \
    -e AWS_DEFAULT_REGION=ap-northeast-2 \
    amazon/aws-cli dynamodb list-tables \
    --endpoint-url http://localhost:8000

echo "DynamoDB Local setup completed!" 