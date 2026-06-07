package com.example.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataInventoryRepository extends JpaRepository<InventoryJpaEntity, UUID> {
}
