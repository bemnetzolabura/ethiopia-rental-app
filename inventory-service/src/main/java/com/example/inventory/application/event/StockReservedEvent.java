package com.example.inventory.application.event;

import java.time.Instant;
import java.util.UUID;

public record StockReservedEvent(
        UUID reservationId,
        UUID orderId,
        Instant occurredAt
) {}
