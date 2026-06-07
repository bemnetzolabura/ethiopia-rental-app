package com.example.notification.infrastructure.messaging;

import com.example.notification.infrastructure.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Bonus: monitors the Dead Letter Queue for failed messages.
 */
@Slf4j
@Component
public class DlqListener {

    @RabbitListener(queues = RabbitMQConfig.DLQ_QUEUE)
    public void onDeadLetter(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String body = new String(message.getBody());
        log.error("☠️ [DLQ] Dead-letter message — routingKey={}, body={}", routingKey, body);
    }
}
