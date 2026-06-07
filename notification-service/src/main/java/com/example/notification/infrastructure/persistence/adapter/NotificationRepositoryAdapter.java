package com.example.notification.infrastructure.persistence.adapter;

import com.example.notification.domain.model.Notification;
import com.example.notification.domain.repository.NotificationRepository;
import com.example.notification.infrastructure.persistence.entity.NotificationEntity;
import com.example.notification.infrastructure.persistence.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    @Override
    public Notification save(Notification notification) {
        jpaRepository.save(toEntity(notification));
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private NotificationEntity toEntity(Notification n) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(n.getId());
        entity.setRoutingKey(n.getRoutingKey());
        entity.setPayload(n.getPayload());
        entity.setMessage(n.getMessage());
        entity.setReceivedAt(n.getReceivedAt());
        return entity;
    }

    private Notification toDomain(NotificationEntity e) {
        return new Notification(e.getId(), e.getRoutingKey(), e.getPayload(),
                e.getMessage(), e.getReceivedAt());
    }
}
