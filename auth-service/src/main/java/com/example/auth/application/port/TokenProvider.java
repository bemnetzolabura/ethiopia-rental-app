package com.example.auth.application.port;

/**
 * Output port for generating authentication tokens.
 * Defined in application layer; implemented in infrastructure (JwtProvider).
 */
public interface TokenProvider {
    String generateToken(String userId, String email, String role);
}
