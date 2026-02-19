package com.bersyte.eventz.features.auth.infrastructure.persistence;

import com.bersyte.eventz.features.users.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record AppUserPrincipal(
        UUID id,
        String email,
        String password,
        UserRole role,
        String firstName,
        String lastName,
        String phone,
        boolean verified,
        LocalDateTime createdAt
) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase())
        );
    }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; }
    
    public UUID getId() { return id; }
    
    @Override
    public String toString() {
        return "AppUserPrincipal[id=" + id + ", email=" + email + "]";
    }
}
