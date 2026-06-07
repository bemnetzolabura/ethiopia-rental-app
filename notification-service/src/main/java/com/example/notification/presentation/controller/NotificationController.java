package com.example.notification.presentation.controller;

import com.example.notification.application.dto.NotificationResponse;
import com.example.notification.application.usecase.GetNotificationsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "View logged event notifications")
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;

    @GetMapping
    @Operation(summary = "List all received event notifications")
    public ResponseEntity<List<NotificationResponse>> listNotifications() {
        return ResponseEntity.ok(getNotificationsUseCase.execute());
    }
}
