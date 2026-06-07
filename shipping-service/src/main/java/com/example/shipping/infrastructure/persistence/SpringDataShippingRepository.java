package com.example.shipping.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataShippingRepository extends JpaRepository<ShippingJpaEntity, UUID> {
}
