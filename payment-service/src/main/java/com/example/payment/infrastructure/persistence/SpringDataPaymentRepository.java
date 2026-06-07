package com.example.payment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataPaymentRepository extends JpaRepository<PaymentJpaEntity, UUID> {
}
