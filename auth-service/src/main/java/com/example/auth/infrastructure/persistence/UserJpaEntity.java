package com.example.auth.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * JPA entity — lives in infrastructure only.
 * Mapped separately from the clean domain entity.
 */
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    public UserJpaEntity(UUID id, String email, String passwordHash, String role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
