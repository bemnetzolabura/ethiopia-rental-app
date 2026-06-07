package com.example.shipping.infrastructure.messaging;

import com.example.shipping.application.event.PaymentCompletedEvent;
import com.example.shipping.application.event.StockReservedEvent;
import com.example.shipping.application.usecase.ProcessShippingStateUseCase;
import com.example.shipping.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShippingEventListener {

    private final ProcessShippingStateUseCase processShippingStateUseCase;

    @RabbitListener(queues = RabbitMQConfig.SHIPPING_QUEUE_PAYMENT)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent in Shipping for order: {}", event.orderId());
        processShippingStateUseCase.processPaymentCompleted(event);
    }

    @RabbitListener(queues = RabbitMQConfig.SHIPPING_QUEUE_STOCK)
    public void onStockReserved(StockReservedEvent event) {
        log.info("Received StockReservedEvent in Shipping for order: {}", event.orderId());
        processShippingStateUseCase.processStockReserved(event);
    }
}
