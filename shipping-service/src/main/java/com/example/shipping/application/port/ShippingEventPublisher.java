package com.example.shipping.application.port;

import com.example.shipping.application.event.ShipmentCreatedEvent;

public interface ShippingEventPublisher {
    void publishShipmentCreated(ShipmentCreatedEvent event);
}
