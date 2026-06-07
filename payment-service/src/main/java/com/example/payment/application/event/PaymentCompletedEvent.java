package com.example.payment.application.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID paymentId,
        UUID orderId,
        String userId,
        BigDecimal amount,
        Instant occurredAt
) {}
