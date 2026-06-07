package com.example.payment.application.usecase;

import com.example.payment.application.event.OrderCreatedEvent;
import com.example.payment.application.event.PaymentCompletedEvent;
import com.example.payment.application.event.PaymentFailedEvent;
import com.example.payment.application.port.PaymentEventPublisher;
import com.example.payment.domain.model.Payment;
import com.example.payment.domain.model.PaymentStatus;
import com.example.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher eventPublisher;

    @Transactional
    public void processPayment(OrderCreatedEvent event) {
        log.info("Processing payment for order: {}", event.orderId());

        Payment payment = new Payment(
                UUID.randomUUID(),
                event.orderId(),
                event.userId(),
                event.totalAmount(),
                PaymentStatus.PENDING,
                Instant.now()
        );

        // Mock payment — always succeeds for reliable end-to-end demos
        boolean isSuccess = true;

        if (isSuccess) {
            payment.complete();
            paymentRepository.save(payment);
            eventPublisher.publishPaymentCompleted(new PaymentCompletedEvent(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getUserId(),
                    payment.getAmount(),
                    Instant.now()
            ));
            log.info("Payment completed for order: {}", event.orderId());
        } else {
            payment.fail();
            paymentRepository.save(payment);
            eventPublisher.publishPaymentFailed(new PaymentFailedEvent(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getUserId(),
                    payment.getAmount(),
                    "Insufficient funds",
                    Instant.now()
            ));
            log.info("Payment failed for order: {}", event.orderId());
        }
    }
}
