package com.example.payment.infrastructure.persistence;

import com.example.payment.domain.model.Payment;
import com.example.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final SpringDataPaymentRepository springDataPaymentRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity entity = new PaymentJpaEntity(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
        PaymentJpaEntity saved = springDataPaymentRepository.save(entity);
        return new Payment(
                saved.getId(),
                saved.getOrderId(),
                saved.getUserId(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
