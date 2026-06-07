package com.example.order.infrastructure.messaging.publisher;

import com.example.order.application.port.EventPublisher;
import com.example.order.infrastructure.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, Object event) {
        log.info("Publishing event to exchange {} with routing key {}: {}", RabbitMQConfig.EXCHANGE_NAME, routingKey, event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, event);
    }
}
