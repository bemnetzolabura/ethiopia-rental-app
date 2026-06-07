package com.example.auth.application.port;

/**
 * Output port for publishing domain events.
 * Defined in application layer; implemented in infrastructure.
 */
public interface EventPublisher {
    void publish(String routingKey, Object event);
}
