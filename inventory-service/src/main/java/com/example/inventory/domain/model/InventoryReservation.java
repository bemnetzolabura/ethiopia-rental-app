package com.example.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservation {
    private UUID id;
    private UUID orderId;
    private InventoryStatus status;
    private Instant createdAt;

    public void reserve() {
        this.status = InventoryStatus.RESERVED;
    }

    public void fail() {
        this.status = InventoryStatus.FAILED;
    }
}
