package com.example.notification.domain.repository;

import com.example.notification.domain.model.Notification;

import java.util.List;

public interface NotificationRepository {

    Notification save(Notification notification);

    List<Notification> findAll();
}
