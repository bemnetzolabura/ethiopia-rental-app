package com.example.shipping.infrastructure.persistence;

import com.example.shipping.domain.model.ShippingState;
import com.example.shipping.domain.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import org.springframework.lang.NonNull;

@Component
@RequiredArgsConstructor
public class ShippingRepositoryImpl implements ShippingRepository {

    private final SpringDataShippingRepository springDataShippingRepository;

    @Override
    public Optional<ShippingState> findByOrderId(@NonNull UUID orderId) {
        return springDataShippingRepository.findById(orderId).map(entity -> new ShippingState(
                entity.getOrderId(),
                entity.isPaymentCompleted(),
                entity.isStockReserved(),
                entity.isShipmentCreated(),
                entity.getShipmentId(),
                entity.getCreatedAt()
        ));
    }

    @Override
    public ShippingState save(ShippingState state) {
        ShippingJpaEntity entity = new ShippingJpaEntity(
                state.getOrderId(),
                state.isPaymentCompleted(),
                state.isStockReserved(),
                state.isShipmentCreated(),
                state.getShipmentId(),
                state.getCreatedAt()
        );
        ShippingJpaEntity saved = springDataShippingRepository.save(entity);
        return new ShippingState(
                saved.getOrderId(),
                saved.isPaymentCompleted(),
                saved.isStockReserved(),
                saved.isShipmentCreated(),
                saved.getShipmentId(),
                saved.getCreatedAt()
        );
    }
}
