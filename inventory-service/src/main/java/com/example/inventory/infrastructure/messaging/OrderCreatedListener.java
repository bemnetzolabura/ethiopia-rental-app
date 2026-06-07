package com.example.inventory.infrastructure.messaging;

import com.example.inventory.application.event.OrderCreatedEvent;
import com.example.inventory.application.usecase.ReserveStockUseCase;
import com.example.inventory.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final ReserveStockUseCase reserveStockUseCase;

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent in Inventory for order: {}", event.orderId());
        reserveStockUseCase.reserveStock(event);
    }
}
