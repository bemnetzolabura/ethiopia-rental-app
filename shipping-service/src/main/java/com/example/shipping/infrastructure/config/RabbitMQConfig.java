package com.example.shipping.infrastructure.config;

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
    public static final String SHIPPING_QUEUE_PAYMENT = "shipping.payment.queue";
    public static final String SHIPPING_QUEUE_STOCK = "shipping.stock.queue";

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
    public Queue shippingPaymentQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(SHIPPING_QUEUE_PAYMENT)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue shippingStockQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(SHIPPING_QUEUE_STOCK)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Binding shippingPaymentBinding(Queue shippingPaymentQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(shippingPaymentQueue).to(appExchange).with("payment.completed");
    }

    @Bean
    public Binding shippingStockBinding(Queue shippingStockQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(shippingStockQueue).to(appExchange).with("stock.reserved");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
