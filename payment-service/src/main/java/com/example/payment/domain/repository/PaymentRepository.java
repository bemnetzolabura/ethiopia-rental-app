package com.example.payment.domain.repository;

import com.example.payment.domain.model.Payment;


public interface PaymentRepository {
    Payment save(Payment payment);
}
