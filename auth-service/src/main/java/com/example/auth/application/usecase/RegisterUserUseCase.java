package com.example.auth.application.usecase;

import com.example.auth.application.dto.RegisterRequest;
import com.example.auth.application.event.UserRegisteredEvent;
import com.example.auth.application.port.EventPublisher;
import com.example.auth.domain.entity.Role;
import com.example.auth.domain.entity.User;
import com.example.auth.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Application use case: register a new user and publish UserRegisteredEvent.
 */
@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    public RegisterUserUseCase(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public User execute(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered: " + request.email());
        }

        User user = new User(
                UUID.randomUUID(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER
        );
        User saved = userRepository.save(user);

        // Publish event via RabbitMQ
        eventPublisher.publish("user.registered",
                new UserRegisteredEvent(saved.getId(), saved.getEmail(),
                        saved.getRole().name(), Instant.now()));

        return saved;
    }
}
