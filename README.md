# Event-Driven Microservices with RabbitMQ & Onion Architecture

A distributed system built with **Spring Boot 3.5**, **RabbitMQ**, and **Onion Architecture** across six independent microservices. Services communicate exclusively via RabbitMQ events — no direct service-to-service REST calls (except client → Auth login).

## Architecture

```
┌─────────────┐     user.registered      ┌──────────────────┐
│ Auth :8081  │ ───────────────────────► │  app.exchange    │
└─────────────┘                          │  (topic)         │
┌─────────────┐     order.created        │                  │
│ Order :8082 │ ───────────────────────► │                  │
└─────────────┘                          └────────┬─────────┘
                                         ┌────────┼────────┐
                    ┌────────────────────┤        │        ├────────────────────┐
                    ▼                    ▼        ▼        ▼                    ▼
            ┌──────────────┐    ┌──────────────┐  ...  ┌──────────────┐  ┌──────────────┐
            │ Payment :8083│    │Inventory:8084│       │ Shipping:8085│  │Notification  │
            └──────┬───────┘    └──────┬───────┘       └──────┬───────┘  │    :8086     │
                   │                   │                      │          └──────────────┘
         payment.* │         stock.*   │                      │ shipment.created
                   └───────────┬───────┘                      │
                               ▼                              ▼
                        Shipping waits for BOTH
                        payment.completed + stock.reserved
```

### Onion Architecture (every service)

| Layer | Responsibility |
|-------|----------------|
| **Domain** | Business logic, entities, repository interfaces — no Spring imports |
| **Application** | Use cases, DTOs, event records, port interfaces |
| **Infrastructure** | JPA, RabbitMQ adapters, security, config |
| **Presentation** | REST controllers (where applicable) |

## Services

| Service | Port | REST API | Events Published | Events Consumed |
|---------|------|----------|------------------|-----------------|
| **auth-service** | 8081 | `POST /auth/register`, `POST /auth/login` | `user.registered` | — |
| **order-service** | 8082 | `POST /api/orders`, `GET /api/orders/{id}` | `order.created` | — |
| **payment-service** | 8083 | — | `payment.completed`, `payment.failed` | `order.created` |
| **inventory-service** | 8084 | — | `stock.reserved`, `stock.failed` | `order.created` |
| **shipping-service** | 8085 | — | `shipment.created` | `payment.completed`, `stock.reserved` |
| **notification-service** | 8086 | `GET /api/notifications` | — | ALL (`#` binding) |

## RabbitMQ Topology

- **Exchange:** `app.exchange` (topic, durable)
- **DLQ Exchange:** `app.dlq.exchange` → queue `app.dlq`

| Routing Key | Publisher | Consumers |
|-------------|-----------|-----------|
| `user.registered` | auth | notification |
| `order.created` | order | payment, inventory, notification |
| `payment.completed` | payment | shipping, notification |
| `payment.failed` | payment | notification |
| `stock.reserved` | inventory | shipping, notification |
| `stock.failed` | inventory | notification |
| `shipment.created` | shipping | notification |

## Quick Start

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose (for full stack)
- RabbitMQ running locally (for manual dev) OR use Docker Compose

### Option 1 — Docker Compose (recommended)

```bash
# Build and start everything (RabbitMQ, PostgreSQL × 5, all services, frontend)
docker compose up --build

# RabbitMQ Management UI: http://localhost:15672  (guest/guest)
# Swagger: http://localhost:8081/swagger-ui.html
```

### Option 2 — Local development (H2, no PostgreSQL)

**RabbitMQ** is required for events. Install it **without Docker**:

```bash
sudo apt update && sudo apt install -y rabbitmq-server
sudo systemctl enable --now rabbitmq-server
```

Or with Docker (if installed):

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
```

Then start all services:

```bash
./run-all.sh
```

`run-all.sh` detects RabbitMQ on `localhost:5672` (Docker or native). If neither is available, it shows install instructions.

Wait ~30 seconds, then test with Postman (see below). All databases are **H2 in-memory** — use `/h2-console` on each service port.

## Testing with Postman

Start the system first (`docker compose up --build` or `./run-all.sh` + RabbitMQ).

### Postman environment (recommended)

Create an environment named **Mini Local** with these variables:

| Variable | Initial value |
|----------|---------------|
| `auth_url` | `http://localhost:8081` |
| `order_url` | `http://localhost:8082` |
| `notification_url` | `http://localhost:8086` |
| `token` | *(leave empty — set after login)* |
| `userId` | *(leave empty — set after register)* |
| `orderId` | *(leave empty — set after create order)* |

---

### Step 1 — Register user

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `{{auth_url}}/auth/register` |
| **Headers** | `Content-Type: application/json` |
| **Body** (raw JSON) | see below |

```json
{
  "email": "alice@example.com",
  "password": "secret123"
}
```

**Expected response:** `201 Created`

```json
{
  "userId": "a1b2c3d4-....",
  "email": "alice@example.com",
  "role": "USER"
}
```

**Postman tip:** In the **Tests** tab, save the user id:

```javascript
const body = pm.response.json();
pm.environment.set("userId", body.userId);
```

Check **notification-service** logs or call Step 5 — you should see a `user.registered` notification.

---

### Step 2 — Login (get JWT)

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `{{auth_url}}/auth/login` |
| **Headers** | `Content-Type: application/json` |
| **Body** (raw JSON) | see below |

```json
{
  "email": "alice@example.com",
  "password": "secret123"
}
```

**Expected response:** `200 OK`

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "email": "alice@example.com",
  "role": "USER"
}
```

**Postman tip:** Save the token for the next requests:

```javascript
const body = pm.response.json();
pm.environment.set("token", body.token);
```

---

### Step 3 — Create order (triggers event flow)

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `{{order_url}}/api/orders` |
| **Headers** | `Content-Type: application/json` |
| | `Authorization: Bearer {{token}}` |
| **Body** (raw JSON) | see below |

```json
{
  "userId": "{{userId}}",
  "items": [
    {
      "productId": "prod-1",
      "quantity": 2,
      "price": 29.99
    }
  ]
}
```

**Expected response:** `201 Created`

```json
{
  "id": "order-uuid-here",
  "userId": "...",
  "status": "PENDING",
  "totalAmount": 59.98,
  "createdAt": "2026-05-16T12:00:00Z"
}
```

**Postman tip:**

```javascript
const body = pm.response.json();
pm.environment.set("orderId", body.id);
```

**What happens next (async via RabbitMQ):**

1. `order.created` → payment-service + inventory-service (parallel)
2. If both succeed → `payment.completed` + `stock.reserved` → shipping-service
3. Shipping publishes `shipment.created`
4. notification-service receives **all** events

Wait **2–5 seconds**, then run Step 4 and Step 5.

---

### Step 4 — Get order by ID

| Field | Value |
|-------|-------|
| **Method** | `GET` |
| **URL** | `{{order_url}}/api/orders/{{orderId}}` |
| **Headers** | `Authorization: Bearer {{token}}` |

**Expected response:** `200 OK` with order details.

---

### Step 5 — List notifications

| Field | Value |
|-------|-------|
| **Method** | `GET` |
| **URL** | `{{notification_url}}/api/notifications` |
| **Headers** | *(none required)* |

**Expected response:** `200 OK` — array of notifications, e.g.:

```json
[
  {
    "id": "...",
    "routingKey": "user.registered",
    "message": "Welcome! A new user has registered.",
    "payload": "{...}",
    "receivedAt": "..."
  },
  {
    "routingKey": "order.created",
    "message": "Your order has been placed.",
    ...
  },
  {
    "routingKey": "payment.completed",
    ...
  },
  {
    "routingKey": "stock.reserved",
    ...
  },
  {
    "routingKey": "shipment.created",
    ...
  }
]
```

> **Note:** Payment and inventory use mock logic (~90% success). If payment or stock fails, you will see `payment.failed` or `stock.failed` instead, and **shipping will not run** until both payment and stock succeed. Run Step 3 again if needed.

---

### Verify in RabbitMQ UI (optional)

1. Open http://localhost:15672 (login: `guest` / `guest`)
2. Go to **Exchanges** → `app.exchange` → **Publish message** (optional)
3. Go to **Queues** — you should see messages flowing through:
   - `payment.queue`
   - `inventory.queue`
   - `shipping.payment.queue`
   - `shipping.stock.queue`
   - `notification.queue`

---

### Swagger (alternative to Postman)

| Service | Swagger UI |
|---------|------------|
| Auth | http://localhost:8081/swagger-ui.html |
| Order | http://localhost:8082/swagger-ui.html |
| Notification | http://localhost:8086/swagger-ui.html |

Use **Authorize** on the order service with: `Bearer <your-token>`.

---

### Common Postman errors

| Status | Cause | Fix |
|--------|-------|-----|
| `401 Unauthorized` on orders | Missing or expired JWT | Run Step 2 again; check `Authorization: Bearer {{token}}` |
| `400 Bad Request` on register | Email already exists | Use a new email or restart auth DB |
| `400 Bad Request` on order | Invalid body | Use `userId`, `productId`, `quantity`, `price` (not `unitPrice`) |
| No notifications | Services not running or RabbitMQ down | Start RabbitMQ + all services; wait a few seconds after Step 3 |
| No `shipment.created` | Payment or stock failed (mock 10% fail rate) | Create another order (Step 3) |

---

### Quick curl equivalent

```bash
# 1. Register
curl -s -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"secret123"}'

# 2. Login → copy token
TOKEN=$(curl -s -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"secret123"}' | jq -r .token)

# 3. Create order (replace USER_ID from step 1)
curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"userId":"USER_ID","items":[{"productId":"prod-1","quantity":2,"price":29.99}]}'

# 4. View notifications
curl -s http://localhost:8086/api/notifications | jq
```

## Bonus Features

- **JWT validation** in order-service (`JwtAuthenticationFilter`)
- **Retry + DLQ** on consumer queues (`x-dead-letter-exchange` + `DqlListener`)
- **Docker Compose** full stack (`docker-compose.yml`)
- **Central file logging** (`logs/<service>.log`)
- **Swagger/OpenAPI** on auth, order, notification services

## Project Structure

```
mini/
├── auth-service/          # Onion: domain → application → infrastructure → presentation
├── order-service/
├── payment-service/
├── inventory-service/
├── shipping-service/
├── notification-service/
├── docker-compose.yml
├── Dockerfile.service
├── run-all.sh
└── frontend/              # React dashboard (optional UI)
```

## Databases (H2 — local default)

**All 6 services use H2 in-memory** when running locally (`./run-all.sh`). No PostgreSQL install required.

| Service | H2 Console URL | JDBC URL (in console) | User | Password |
|---------|----------------|----------------------|------|----------|
| auth | http://localhost:8081/h2-console | `jdbc:h2:mem:authdb` | `sa` | *(empty)* |
| order | http://localhost:8082/h2-console | `jdbc:h2:mem:orderdb` | `sa` | *(empty)* |
| payment | http://localhost:8083/h2-console | `jdbc:h2:mem:paymentdb` | `sa` | *(empty)* |
| inventory | http://localhost:8084/h2-console | `jdbc:h2:mem:inventorydb` | `sa` | *(empty)* |
| shipping | http://localhost:8085/h2-console | `jdbc:h2:mem:shippingdb` | `sa` | *(empty)* |
| notification | http://localhost:8086/h2-console | `jdbc:h2:mem:notificationdb` | `sa` | *(empty)* |

**Docker Compose** uses profile `docker` → PostgreSQL per service (ports 5433–5437). Notification stays on H2.

> **Fixed issues:** JWT validation on order-service now matches auth signing; payment/inventory/shipping no longer require local PostgreSQL.
# ethiopia-rental-app
