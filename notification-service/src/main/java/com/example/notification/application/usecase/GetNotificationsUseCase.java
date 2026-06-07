package com.example.notification.application.usecase;

import com.example.notification.application.dto.NotificationResponse;
import com.example.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> execute() {
        return notificationRepository.findAll().stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getRoutingKey(),
                        n.getMessage(),
                        n.getPayload(),
                        n.getReceivedAt()))
                .toList();
    }
}
