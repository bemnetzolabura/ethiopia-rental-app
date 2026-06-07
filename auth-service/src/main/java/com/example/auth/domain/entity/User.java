package com.example.auth.domain.entity;

import java.util.UUID;

/**
 * Domain entity — pure Java, no framework annotations.
 * Represents a registered user in the system.
 */
public class User {

    private final UUID id;
    private final String email;
    private String passwordHash;
    private final Role role;

    public User(UUID id, String email, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
}
