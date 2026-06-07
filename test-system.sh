#!/bin/bash

# Configuration
AUTH_URL="http://localhost:8081/auth"
ORDER_URL="http://localhost:8082/api/orders"

echo "🚀 Starting System Test Flow..."

# 1. Register User
echo "1. Registering user..."
curl -s -X POST "$AUTH_URL/register" \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123", "fullName": "Test User"}' | jq .

# 2. Login
echo -e "\n2. Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST "$AUTH_URL/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123"}')

TOKEN=$(echo $LOGIN_RESPONSE | jq -r .token)
echo "Token acquired: ${TOKEN:0:20}..."

# 3. Create Order
echo -e "\n3. Creating Order..."
curl -s -X POST "$ORDER_URL" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "userId": "test-user-id",
    "items": [
      {"productId": "prod-001", "quantity": 2, "price": 49.99},
      {"productId": "prod-002", "quantity": 1, "price": 99.99}
    ]
  }' | jq .

echo -e "\n✅ Flow initiated. Check service logs for event processing details."
