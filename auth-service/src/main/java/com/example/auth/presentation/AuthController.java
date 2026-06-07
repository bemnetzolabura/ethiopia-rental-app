package com.example.auth.presentation;

import com.example.auth.application.dto.LoginRequest;
import com.example.auth.application.dto.LoginResponse;
import com.example.auth.application.dto.RegisterRequest;
import com.example.auth.application.usecase.LoginUseCase;
import com.example.auth.application.usecase.RegisterUserUseCase;
import com.example.auth.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Presentation layer — REST controller for auth operations.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User registration and login")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "userId", user.getId(),
                        "email",  user.getEmail(),
                        "role",   user.getRole()
                ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive a JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
