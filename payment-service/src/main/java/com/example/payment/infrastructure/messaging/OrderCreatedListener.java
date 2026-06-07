package com.example.payment.infrastructure.messaging;

import com.example.payment.application.event.OrderCreatedEvent;
import com.example.payment.application.usecase.ProcessPaymentUseCase;
import com.example.payment.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order: {}", event.orderId());
        processPaymentUseCase.processPayment(event);
    }
}
