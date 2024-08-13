package com.bersyte.eventz.security.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Integer> {
    AuthUser findByEmail(String email);
}
