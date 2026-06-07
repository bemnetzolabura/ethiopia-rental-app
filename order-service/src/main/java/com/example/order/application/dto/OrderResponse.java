package com.example.order.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String userId,
        String status,
        BigDecimal totalAmount,
        Instant createdAt
) {}
