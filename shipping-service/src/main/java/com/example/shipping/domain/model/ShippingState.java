package com.example.shipping.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingState {
    private UUID orderId;
    private boolean paymentCompleted;
    private boolean stockReserved;
    private boolean shipmentCreated;
    private UUID shipmentId;
    private Instant createdAt;

    public void markPaymentCompleted() {
        this.paymentCompleted = true;
    }

    public void markStockReserved() {
        this.stockReserved = true;
    }

    public boolean canShip() {
        return paymentCompleted && stockReserved && !shipmentCreated;
    }

    public void markShipped(UUID shipmentId) {
        this.shipmentCreated = true;
        this.shipmentId = shipmentId;
    }
}
