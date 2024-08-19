package com.bersyte.eventz.auth;

import com.bersyte.eventz.common.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);
}
