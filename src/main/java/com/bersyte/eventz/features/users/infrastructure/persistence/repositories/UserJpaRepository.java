package com.bersyte.eventz.features.users.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.auth.infrastructure.persistence.AppUserPrincipal;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    
    @Query("SELECT u.id, u.email, u.password, u.role FROM UserEntity u WHERE u.email = :email")
    Optional<AppUserPrincipal> findPrincipalByEmail(@Param("email") String email);
    
    boolean existsByEmail(String email);
}
