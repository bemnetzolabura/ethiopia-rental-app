package com.example.inventory.infrastructure.persistence;

import com.example.inventory.domain.model.InventoryReservation;
import com.example.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

    private final SpringDataInventoryRepository springDataInventoryRepository;

    @Override
    public InventoryReservation save(InventoryReservation reservation) {
        InventoryJpaEntity entity = new InventoryJpaEntity(
                reservation.getId(),
                reservation.getOrderId(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
        InventoryJpaEntity saved = springDataInventoryRepository.save(entity);
        return new InventoryReservation(
                saved.getId(),
                saved.getOrderId(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
