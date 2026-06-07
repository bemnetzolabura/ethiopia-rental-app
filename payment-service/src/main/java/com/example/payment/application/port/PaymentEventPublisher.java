package com.example.payment.application.port;

import com.example.payment.application.event.PaymentCompletedEvent;
import com.example.payment.application.event.PaymentFailedEvent;

public interface PaymentEventPublisher {
    void publishPaymentCompleted(PaymentCompletedEvent event);
    void publishPaymentFailed(PaymentFailedEvent event);
}
