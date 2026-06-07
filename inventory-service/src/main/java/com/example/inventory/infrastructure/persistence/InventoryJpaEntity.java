package com.example.inventory.infrastructure.persistence;

import com.example.inventory.domain.model.InventoryStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryJpaEntity {
    @Id
    private UUID id;
    private UUID orderId;
    
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
    private Instant createdAt;
}
