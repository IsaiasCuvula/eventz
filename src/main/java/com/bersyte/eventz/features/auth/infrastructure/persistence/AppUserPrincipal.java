package com.bersyte.eventz.features.auth.infrastructure.persistence;

import com.bersyte.eventz.features.users.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AppUserPrincipal(
        String id,
        String email,
        String password,
        UserRole role
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
    
    public String getId() { return id; }
    
    @Override
    public String toString() {
        return "AppUserPrincipal[id=" + id + ", email=" + email + "]";
    }
}
