package com.example.order.application.usecase;

import com.example.order.application.dto.CreateOrderRequest;
import com.example.order.application.event.OrderCreatedEvent;
import com.example.order.application.port.EventPublisher;
import com.example.order.domain.entity.Order;
import com.example.order.domain.entity.OrderItem;
import com.example.order.domain.entity.OrderStatus;
import com.example.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Application use case: create an order and publish OrderCreatedEvent.
 */
@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public CreateOrderUseCase(OrderRepository orderRepository, EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher  = eventPublisher;
    }

    public Order execute(CreateOrderRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(i -> new OrderItem(i.productId(), i.quantity(), i.price()))
                .toList();

        Order order = new Order(UUID.randomUUID(), request.userId(),
                items, OrderStatus.PENDING, Instant.now());

        Order saved = orderRepository.save(order);

        // Build event items
        List<OrderCreatedEvent.OrderItemDto> eventItems = saved.getItems().stream()
                .map(i -> new OrderCreatedEvent.OrderItemDto(i.getProductId(), i.getQuantity(), i.getPrice()))
                .toList();

        eventPublisher.publish("order.created", new OrderCreatedEvent(
                saved.getId(), saved.getUserId(), eventItems,
                saved.totalAmount(), Instant.now()));

        return saved;
    }
}
