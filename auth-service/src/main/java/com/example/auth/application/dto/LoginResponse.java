package com.example.auth.application.dto;

public record LoginResponse(
        String token,
        String email,
        String role
) {}
