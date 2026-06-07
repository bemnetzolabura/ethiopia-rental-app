package com.example.notification.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class NotificationEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String routingKey;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private Instant receivedAt;
}
