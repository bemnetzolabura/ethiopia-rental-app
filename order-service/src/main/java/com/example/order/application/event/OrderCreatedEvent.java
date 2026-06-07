package com.example.order.application.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Event published when an order is created.
 * Routing key: order.created
 */
public record OrderCreatedEvent(
        UUID orderId,
        String userId,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        Instant occurredAt
) {
    public record OrderItemDto(String productId, int quantity, BigDecimal price) {}
}
