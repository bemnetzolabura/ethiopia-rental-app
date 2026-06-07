package com.example.shipping.domain.repository;

import com.example.shipping.domain.model.ShippingState;
import java.util.Optional;
import org.springframework.lang.NonNull;
import java.util.UUID;

public interface ShippingRepository {
    Optional<ShippingState> findByOrderId(@NonNull UUID orderId);
    ShippingState save(ShippingState state);
}
