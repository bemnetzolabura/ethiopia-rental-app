package com.example.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private UUID id;
    private UUID orderId;
    private String userId;
    private BigDecimal amount;
    private PaymentStatus status;
    private Instant createdAt;

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}
