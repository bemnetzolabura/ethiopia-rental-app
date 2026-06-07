package com.example.shipping.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipping_states")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingJpaEntity {
    @Id
    private UUID orderId;
    private boolean paymentCompleted;
    private boolean stockReserved;
    private boolean shipmentCreated;
    private UUID shipmentId;
    private Instant createdAt;
}
