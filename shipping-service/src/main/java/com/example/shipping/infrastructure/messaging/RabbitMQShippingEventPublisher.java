package com.example.shipping.infrastructure.messaging;

import com.example.shipping.application.event.ShipmentCreatedEvent;
import com.example.shipping.application.port.ShippingEventPublisher;
import com.example.shipping.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQShippingEventPublisher implements ShippingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishShipmentCreated(ShipmentCreatedEvent event) {
        log.info("Publishing ShipmentCreatedEvent for order: {}", event.orderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "shipment.created", event);
    }
}
