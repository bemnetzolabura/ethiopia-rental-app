package com.example.auth.domain.repository;

import com.example.auth.domain.entity.User;

import java.util.Optional;

/**
 * Domain repository interface — defined in domain, implemented in infrastructure.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
