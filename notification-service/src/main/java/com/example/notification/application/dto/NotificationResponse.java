package com.example.notification.application.dto;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String routingKey,
        String message,
        String payload,
        Instant receivedAt
) {}
