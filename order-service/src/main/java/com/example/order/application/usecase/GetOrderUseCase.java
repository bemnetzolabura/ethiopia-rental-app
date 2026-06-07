package com.example.order.application.usecase;

import com.example.order.domain.entity.Order;
import com.example.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> execute(UUID id) {
        return orderRepository.findById(id);
    }
}
