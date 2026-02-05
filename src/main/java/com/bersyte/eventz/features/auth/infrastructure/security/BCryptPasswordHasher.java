package com.bersyte.eventz.features.auth.infrastructure.security;

import com.bersyte.eventz.features.auth.domain.service.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
    private final PasswordEncoder springEncoder;
    
    public BCryptPasswordHasher(PasswordEncoder springEncoder) {
        this.springEncoder = springEncoder;
    }
    
    @Override
    public String encode(String rawPassword) {
        return springEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springEncoder.matches(rawPassword, encodedPassword);
    }
}
