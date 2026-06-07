package com.example.shipping.application.event;

import org.springframework.lang.NonNull;
import java.time.Instant;
import java.util.UUID;

public record StockReservedEvent(
        UUID reservationId,
        @NonNull UUID orderId,
        Instant occurredAt
) {}
