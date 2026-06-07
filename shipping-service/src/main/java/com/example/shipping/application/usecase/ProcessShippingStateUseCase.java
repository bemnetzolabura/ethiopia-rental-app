package com.example.shipping.application.usecase;

import com.example.shipping.application.event.PaymentCompletedEvent;
import com.example.shipping.application.event.ShipmentCreatedEvent;
import com.example.shipping.application.event.StockReservedEvent;
import com.example.shipping.application.port.ShippingEventPublisher;
import com.example.shipping.domain.model.ShippingState;
import com.example.shipping.domain.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessShippingStateUseCase {

    private final ShippingRepository shippingRepository;
    private final ShippingEventPublisher eventPublisher;

    @Transactional
    public void processPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Processing PaymentCompletedEvent for order: {}", event.orderId());
        ShippingState state = shippingRepository.findByOrderId(event.orderId())
                .orElseGet(() -> new ShippingState(event.orderId(), false, false, false, null, Instant.now()));

        state.markPaymentCompleted();
        checkAndShip(state);
    }

    @Transactional
    public void processStockReserved(StockReservedEvent event) {
        log.info("Processing StockReservedEvent for order: {}", event.orderId());
        ShippingState state = shippingRepository.findByOrderId(event.orderId())
                .orElseGet(() -> new ShippingState(event.orderId(), false, false, false, null, Instant.now()));

        state.markStockReserved();
        checkAndShip(state);
    }

    private void checkAndShip(ShippingState state) {
        if (state.canShip()) {
            UUID shipmentId = UUID.randomUUID();
            state.markShipped(shipmentId);
            log.info("Order {} is ready for shipment. Created shipment: {}", state.getOrderId(), shipmentId);
            
            eventPublisher.publishShipmentCreated(new ShipmentCreatedEvent(
                    shipmentId,
                    state.getOrderId(),
                    Instant.now()
            ));
        }
        shippingRepository.save(state);
    }
}
