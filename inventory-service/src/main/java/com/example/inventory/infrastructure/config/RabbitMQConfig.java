package com.example.inventory.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "app.exchange";
    public static final String INVENTORY_QUEUE = "inventory.queue";

    public static final String DLQ_EXCHANGE = "app.dlq.exchange";
    public static final String DLQ_QUEUE = "app.dlq";

    @Bean
    public org.springframework.amqp.core.DirectExchange dlqExchange() {
        return new org.springframework.amqp.core.DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Queue dlqQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with(DLQ_QUEUE);
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue inventoryQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(INVENTORY_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(inventoryQueue).to(appExchange).with("order.created");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
