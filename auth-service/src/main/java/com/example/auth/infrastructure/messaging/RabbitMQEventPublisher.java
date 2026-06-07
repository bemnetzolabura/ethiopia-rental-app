package com.example.auth.infrastructure.messaging;

import com.example.auth.application.port.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, Object event) {
        log.info("[AUTH] Publishing event '{}': {}", routingKey, event);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
