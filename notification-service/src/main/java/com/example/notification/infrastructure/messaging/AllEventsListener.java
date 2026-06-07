package com.example.notification.infrastructure.messaging;

import com.example.notification.application.usecase.ProcessIncomingEventUseCase;
import com.example.notification.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Infrastructure adapter — receives RabbitMQ messages and delegates to the application layer.
 */
@Component
@RequiredArgsConstructor
public class AllEventsListener {

    private final ProcessIncomingEventUseCase processIncomingEventUseCase;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void onEvent(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String body = new String(message.getBody());
        processIncomingEventUseCase.execute(routingKey, body);
    }
}
