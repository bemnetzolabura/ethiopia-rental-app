package com.example.payment.infrastructure.messaging;

import com.example.payment.application.event.PaymentCompletedEvent;
import com.example.payment.application.event.PaymentFailedEvent;
import com.example.payment.application.port.PaymentEventPublisher;
import com.example.payment.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQPaymentEventPublisher implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Publishing PaymentCompletedEvent for order: {}", event.orderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "payment.completed", event);
    }

    @Override
    public void publishPaymentFailed(PaymentFailedEvent event) {
        log.info("Publishing PaymentFailedEvent for order: {}", event.orderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "payment.failed", event);
    }
}
