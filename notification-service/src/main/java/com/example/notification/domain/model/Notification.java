package com.example.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain entity — pure business object, no framework dependencies.
 */
public class Notification {

    private final UUID id;
    private final String routingKey;
    private final String payload;
    private final String message;
    private final Instant receivedAt;

    public Notification(UUID id, String routingKey, String payload, String message, Instant receivedAt) {
        this.id = id;
        this.routingKey = routingKey;
        this.payload = payload;
        this.message = message;
        this.receivedAt = receivedAt;
    }

    public static Notification fromEvent(String routingKey, String payload) {
        String message = buildMessage(routingKey, payload);
        return new Notification(UUID.randomUUID(), routingKey, payload, message, Instant.now());
    }

    private static String buildMessage(String routingKey, String payload) {
        return switch (routingKey) {
            case "user.registered"   -> "Welcome! A new user has registered.";
            case "order.created"       -> "Your order has been placed.";
            case "payment.completed"   -> "Payment was successful.";
            case "payment.failed"      -> "Payment failed. Please try again.";
            case "stock.reserved"      -> "Items reserved in inventory.";
            case "stock.failed"        -> "Stock unavailable for your order.";
            case "shipment.created"    -> "Your order has been shipped!";
            default                    -> "System event received: " + routingKey;
        };
    }

    public UUID getId() { return id; }
    public String getRoutingKey() { return routingKey; }
    public String getPayload() { return payload; }
    public String getMessage() { return message; }
    public Instant getReceivedAt() { return receivedAt; }
}
