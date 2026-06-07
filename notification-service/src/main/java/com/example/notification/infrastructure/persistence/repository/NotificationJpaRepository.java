package com.example.notification.infrastructure.persistence.repository;

import com.example.notification.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, UUID> {}
