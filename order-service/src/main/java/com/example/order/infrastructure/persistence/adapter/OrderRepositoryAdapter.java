package com.example.order.infrastructure.persistence.adapter;

import com.example.order.domain.entity.Order;
import com.example.order.domain.entity.OrderItem;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.persistence.entity.OrderEntity;
import com.example.order.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        Objects.requireNonNull(order, "Order must not be null");
        OrderEntity entity = toEntity(order);
        OrderEntity saved = jpaRepository.save(Objects.requireNonNull(entity));
        return toDomain(Objects.requireNonNull(saved));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private OrderEntity toEntity(Order order) {
        List<OrderEntity.OrderItemEntity> items = order.getItems().stream()
                .map(i -> new OrderEntity.OrderItemEntity(null, i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        return new OrderEntity(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.totalAmount(),
                order.getCreatedAt(),
                items
        );
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        return new Order(
                entity.getId(),
                entity.getUserId(),
                items,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
