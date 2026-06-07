package com.example.shipping.application.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreatedEvent(
        UUID shipmentId,
        UUID orderId,
        Instant occurredAt
) {}
