package com.example.inventory.application.usecase;

import com.example.inventory.application.event.OrderCreatedEvent;
import com.example.inventory.application.event.StockFailedEvent;
import com.example.inventory.application.event.StockReservedEvent;
import com.example.inventory.application.port.InventoryEventPublisher;
import com.example.inventory.domain.model.InventoryReservation;
import com.example.inventory.domain.model.InventoryStatus;
import com.example.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveStockUseCase {

    private final InventoryRepository inventoryRepository;
    private final InventoryEventPublisher eventPublisher;

    @Transactional
    public void reserveStock(OrderCreatedEvent event) {
        log.info("Reserving stock for order: {}", event.orderId());

        InventoryReservation reservation = new InventoryReservation(
                UUID.randomUUID(),
                event.orderId(),
                InventoryStatus.PENDING,
                Instant.now()
        );

        // Mock stock check — always succeeds for reliable end-to-end demos
        boolean isAvailable = true;

        if (isAvailable) {
            reservation.reserve();
            inventoryRepository.save(reservation);
            eventPublisher.publishStockReserved(new StockReservedEvent(
                    reservation.getId(),
                    reservation.getOrderId(),
                    Instant.now()
            ));
            log.info("Stock reserved for order: {}", event.orderId());
        } else {
            reservation.fail();
            inventoryRepository.save(reservation);
            eventPublisher.publishStockFailed(new StockFailedEvent(
                    reservation.getId(),
                    reservation.getOrderId(),
                    "Out of stock",
                    Instant.now()
            ));
            log.info("Stock reservation failed for order: {}", event.orderId());
        }
    }
}
