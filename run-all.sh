#!/bin/bash

echo "🚀 Starting Event-Driven Microservices (H2 + RabbitMQ local mode)"
echo ""

mkdir -p logs

# ── Helpers ───────────────────────────────────────────────────────────────────
rabbitmq_ready() {
  (echo >/dev/tcp/localhost/5672) 2>/dev/null
}

wait_for_rabbitmq() {
  local i
  for i in $(seq 1 30); do
    if rabbitmq_ready; then
      return 0
    fi
    sleep 1
  done
  return 1
}

start_rabbitmq() {
  if rabbitmq_ready; then
    echo "✓ RabbitMQ already running on localhost:5672"
    return 0
  fi

  # Option 1: Docker (if installed)
  if command -v docker &>/dev/null; then
    if docker ps --format '{{.Names}}' 2>/dev/null | grep -q '^rabbitmq$'; then
      echo "✓ RabbitMQ Docker container already running."
      return 0
    fi
    if docker ps -a --format '{{.Names}}' 2>/dev/null | grep -q '^rabbitmq$'; then
      echo "Starting existing RabbitMQ container..."
      docker start rabbitmq
    else
      echo "Starting RabbitMQ via Docker..."
      docker run -d --name rabbitmq \
        -p 5672:5672 -p 15672:15672 \
        rabbitmq:3.13-management
    fi
    echo "Waiting for RabbitMQ..."
    if wait_for_rabbitmq; then
      echo "✓ RabbitMQ ready (Docker). UI: http://localhost:15672 (guest/guest)"
      return 0
    fi
    echo "⚠ Docker RabbitMQ did not become ready in time."
    return 1
  fi

  # Option 2: Native RabbitMQ (apt install rabbitmq-server)
  if command -v rabbitmqctl &>/dev/null; then
    echo "Starting RabbitMQ via system service..."
    if command -v systemctl &>/dev/null; then
      sudo systemctl start rabbitmq-server 2>/dev/null || systemctl --user start rabbitmq-server 2>/dev/null || true
    fi
    if wait_for_rabbitmq; then
      echo "✓ RabbitMQ ready (native). UI: http://localhost:15672 (guest/guest)"
      return 0
    fi
  fi

  # Not available
  echo ""
  echo "⚠ RabbitMQ is not running and Docker is not installed."
  echo ""
  echo "  Install RabbitMQ (pick one):"
  echo "    sudo apt update && sudo apt install -y rabbitmq-server"
  echo "    sudo systemctl enable --now rabbitmq-server"
  echo ""
  echo "  Or install Docker, then re-run: ./run-all.sh"
  echo ""
  echo "  Services will still start, but events (payment, shipping, notifications)"
  echo "  will NOT work until RabbitMQ is on localhost:5672."
  echo ""
  read -r -p "Continue without RabbitMQ? [y/N] " answer
  case "$answer" in
    y|Y|yes|Yes) return 0 ;;
    *) echo "Aborted. Start RabbitMQ first, then run ./run-all.sh again."; exit 1 ;;
  esac
}

# ── RabbitMQ ──────────────────────────────────────────────────────────────────
start_rabbitmq

# ── Build ─────────────────────────────────────────────────────────────────────
echo ""
echo "Building services..."
if ! ./mvnw clean package -DskipTests -q; then
  echo "❌ Maven build failed. Check errors above."
  exit 1
fi

# ── Stop previous instances on same ports ─────────────────────────────────────
pkill -f 'auth-service.*spring-boot:run' 2>/dev/null || true
pkill -f 'order-service.*spring-boot:run' 2>/dev/null || true
pkill -f 'payment-service.*spring-boot:run' 2>/dev/null || true
pkill -f 'inventory-service.*spring-boot:run' 2>/dev/null || true
pkill -f 'shipping-service.*spring-boot:run' 2>/dev/null || true
pkill -f 'notification-service.*spring-boot:run' 2>/dev/null || true
sleep 2

# ── Start services (H2 in-memory, no PostgreSQL required) ───────────────────
./mvnw -pl auth-service spring-boot:run > logs/auth-service.log 2>&1 &
echo "✓ Auth Service      → http://localhost:8081  (H2: /h2-console)"
sleep 8

./mvnw -pl order-service spring-boot:run > logs/order-service.log 2>&1 &
echo "✓ Order Service     → http://localhost:8082  (H2: /h2-console)"
sleep 8

./mvnw -pl payment-service spring-boot:run > logs/payment-service.log 2>&1 &
echo "✓ Payment Service   → http://localhost:8083  (H2: /h2-console)"

./mvnw -pl inventory-service spring-boot:run > logs/inventory-service.log 2>&1 &
echo "✓ Inventory Service → http://localhost:8084  (H2: /h2-console)"

./mvnw -pl shipping-service spring-boot:run > logs/shipping-service.log 2>&1 &
echo "✓ Shipping Service  → http://localhost:8085  (H2: /h2-console)"

./mvnw -pl notification-service spring-boot:run > logs/notification-service.log 2>&1 &
echo "✓ Notification Svc  → http://localhost:8086  (H2: /h2-console)"

echo ""
echo "✅ All services starting. Wait ~30s then test with Postman (see README.md)."
echo "   Logs: tail -f logs/auth-service.log"
echo "   Stop:  pkill -f 'spring-boot:run'"
