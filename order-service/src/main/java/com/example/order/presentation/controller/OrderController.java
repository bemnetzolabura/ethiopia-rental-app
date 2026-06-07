package com.example.order.presentation.controller;

import com.example.order.application.dto.CreateOrderRequest;
import com.example.order.application.dto.OrderResponse;
import com.example.order.application.usecase.CreateOrderUseCase;
import com.example.order.application.usecase.GetOrderUseCase;
import com.example.order.domain.entity.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponse(
                        order.getId(),
                        order.getUserId(),
                        order.getStatus().name(),
                        order.totalAmount(),
                        order.getCreatedAt()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        return getOrderUseCase.execute(id)
                .map(order -> ResponseEntity.ok(new OrderResponse(
                        order.getId(),
                        order.getUserId(),
                        order.getStatus().name(),
                        order.totalAmount(),
                        order.getCreatedAt()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
