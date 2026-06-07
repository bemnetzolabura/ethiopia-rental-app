package com.example.notification.application.usecase;

import com.example.notification.domain.model.Notification;
import com.example.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessIncomingEventUseCase {

    private final NotificationRepository notificationRepository;

    public Notification execute(String routingKey, String payload) {
        Notification notification = Notification.fromEvent(routingKey, payload);
        notificationRepository.save(notification);
        log.info("🔔 [NOTIFICATION] {} — {}", routingKey, notification.getMessage());
        return notification;
    }
}
