package com.example.payment.infrastructure.persistence;

import com.example.payment.domain.model.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentJpaEntity {
    @Id
    private UUID id;
    private UUID orderId;
    private String userId;
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Instant createdAt;
}
