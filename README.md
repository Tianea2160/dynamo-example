# DynamoDB Example with Spring Boot and Kotlin

이 프로젝트는 Spring Boot와 Kotlin을 사용하여 Amazon DynamoDB를 활용하는 예제 프로젝트입니다.

## 기술 스택

- Kotlin
- Spring Boot
- Amazon DynamoDB
- Docker (DynamoDB Local)

## 주요 기능

### 1. Order 관리 시스템
- Order와 OrderItem의 1:N 관계를 DynamoDB에서 구현
- 중첩 문서 구조를 활용한 데이터 모델링
- Order 상태 관리 (PENDING, PAID, SHIPPED, DELIVERED, CANCELLED)

### 2. Global Secondary Index (GSI) 활용
- `status-index`: Order 상태 기반 조회
  - Hash Key: status
  - Range Key: orderDate
- 다양한 쿼리 패턴 지원
  - 특정 상태의 주문 조회
  - 날짜 범위 기반 주문 조회
  - 상태별 주문 통계

## 프로젝트 구조

```
├── src/
│   └── main/
│       └── kotlin/
│           └── org/tianea/dynamoexample/
│               ├── model/
│               │   └── Order.kt         # Order 및 OrderItem 모델
│               ├── repository/          # DynamoDB 리포지토리
│               ├── service/             # 비즈니스 로직
│               └── controller/          # REST API 엔드포인트
├── scripts/
│   ├── test-gsi.sh                     # GSI 테스트 스크립트
│   └── setup-local.sh                  # Local 환경 설정 스크립트
└── docker-compose.yml                   # DynamoDB Local 설정
```

## 시작하기

### 1. 사전 요구사항
- Docker
- JDK 17 이상
- Kotlin

### 2. DynamoDB Local 실행
```bash
docker-compose up -d
```

### 3. 테이블 생성 및 초기 데이터 설정
```bash
./scripts/setup-local.sh
```

### 4. 애플리케이션 실행
```bash
./gradlew bootRun
```

## API 엔드포인트

### Order API
- `POST /api/orders`: 새로운 주문 생성
- `GET /api/orders/{userId}`: 사용자의 주문 목록 조회
- `GET /api/orders/status/{status}`: 특정 상태의 주문 조회
- `PUT /api/orders/{orderId}/status`: 주문 상태 업데이트

## DynamoDB 모델링

### Order 테이블
- Primary Key
  - Hash Key: userId
  - Range Key: orderId
- GSI (status-index)
  - Hash Key: status
  - Range Key: orderDate

### 데이터 구조
```json
{
  "userId": "USER123",
  "orderId": "ORDER123",
  "orderDate": "2024-05-24T14:30:00",
  "status": "PENDING",
  "items": [
    {
      "productId": "PROD-1",
      "productName": "Example Product",
      "quantity": 2,
      "price": 29.99
    }
  ],
  "totalAmount": 59.98
}
```

## 테스트

GSI 쿼리 테스트:
```bash
./scripts/test-gsi.sh
```

## 라이선스

This project is licensed under the MIT License - see the LICENSE file for details. 