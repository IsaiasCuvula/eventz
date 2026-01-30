package com.bersyte.eventz.features.users.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
