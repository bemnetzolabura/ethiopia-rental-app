package com.example.order.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Domain entity — pure Java, no framework annotations.
 */
public class Order {
    private final UUID id;
    private final String userId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Instant createdAt;

    public Order(UUID id, String userId, List<OrderItem> items,
                 OrderStatus status, Instant createdAt) {
        this.id        = id;
        this.userId    = userId;
        this.items     = items;
        this.status    = status;
        this.createdAt = createdAt;
    }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId()           { return id; }
    public String getUserId()     { return userId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus(){ return status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
