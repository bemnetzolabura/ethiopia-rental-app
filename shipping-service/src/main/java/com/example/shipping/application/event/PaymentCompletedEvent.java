package com.example.shipping.application.event;

import org.springframework.lang.NonNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID paymentId,
        @NonNull UUID orderId,
        String userId,
        BigDecimal amount,
        Instant occurredAt
) {}
