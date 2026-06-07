package com.example.auth.application.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Application-layer event published when a user registers.
 * Serialised to JSON and sent via RabbitMQ with routing key "user.registered".
 */
public record UserRegisteredEvent(
        UUID userId,
        String email,
        String role,
        Instant occurredAt
) {}
