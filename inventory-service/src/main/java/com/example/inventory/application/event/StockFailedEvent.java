package com.example.inventory.application.event;

import java.time.Instant;
import java.util.UUID;

public record StockFailedEvent(
        UUID reservationId,
        UUID orderId,
        String reason,
        Instant occurredAt
) {}
