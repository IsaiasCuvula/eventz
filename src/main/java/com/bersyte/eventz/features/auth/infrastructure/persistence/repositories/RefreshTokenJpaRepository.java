package com.bersyte.eventz.features.auth.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, String> {
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity  r WHERE r.user.id = :userId")
    void deleteAllByUserId(UUID userId);
}
