package com.bersyte.eventz.features.users.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.auth.infrastructure.persistence.dtos.AppUserPrincipal;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    
    @Query("SELECT u.id, u.email, u.password, u.role, u.firstName, u.lastName, u.phone, u.verified, u.createdAt FROM UserEntity u WHERE u.email = :email")
    Optional<AppUserPrincipal> findPrincipalByEmail(@Param("email") String email);
    
    boolean existsByEmail(String email);
}
