package com.example.inventory.infrastructure.messaging;

import com.example.inventory.application.event.StockFailedEvent;
import com.example.inventory.application.event.StockReservedEvent;
import com.example.inventory.application.port.InventoryEventPublisher;
import com.example.inventory.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQInventoryEventPublisher implements InventoryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishStockReserved(StockReservedEvent event) {
        log.info("Publishing StockReservedEvent for order: {}", event.orderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "stock.reserved", event);
    }

    @Override
    public void publishStockFailed(StockFailedEvent event) {
        log.info("Publishing StockFailedEvent for order: {}", event.orderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "stock.failed", event);
    }
}
