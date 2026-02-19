package com.bersyte.eventz.features.auth.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, String> {
    
    @Modifying
    void deleteAllByUserId(UUID userId);
}
