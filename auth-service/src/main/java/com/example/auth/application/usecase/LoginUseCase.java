package com.example.auth.application.usecase;

import com.example.auth.application.dto.LoginRequest;
import com.example.auth.application.dto.LoginResponse;
import com.example.auth.application.port.TokenProvider;
import com.example.auth.domain.entity.User;
import com.example.auth.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Application use case: authenticate a user and return a JWT token.
 */
@Service
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public LoginUseCase(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(
                user.getId().toString(), user.getEmail(), user.getRole().name());

        return new LoginResponse(token, user.getEmail(), user.getRole().name());
    }
}
