package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.InventoryReservation;


public interface InventoryRepository {
    InventoryReservation save(InventoryReservation reservation);
}
