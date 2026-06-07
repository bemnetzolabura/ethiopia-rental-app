package com.example.auth.infrastructure.persistence;

import com.example.auth.domain.entity.Role;
import com.example.auth.domain.entity.User;
import com.example.auth.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Infrastructure adapter implementing the domain's UserRepository port.
 * Translates between domain User and JPA UserJpaEntity.
 */
@Component
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toJpa(user);
        UserJpaEntity saved = jpaRepository.save(java.util.Objects.requireNonNull(entity));
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserJpaEntity toJpa(User u) {
        return new UserJpaEntity(u.getId(), u.getEmail(), u.getPasswordHash(), u.getRole().name());
    }

    private User toDomain(UserJpaEntity e) {
        return new User(e.getId(), e.getEmail(), e.getPasswordHash(), Role.valueOf(e.getRole()));
    }
}
