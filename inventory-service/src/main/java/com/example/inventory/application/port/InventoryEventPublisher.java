package com.example.inventory.application.port;

import com.example.inventory.application.event.StockFailedEvent;
import com.example.inventory.application.event.StockReservedEvent;

public interface InventoryEventPublisher {
    void publishStockReserved(StockReservedEvent event);
    void publishStockFailed(StockFailedEvent event);
}
