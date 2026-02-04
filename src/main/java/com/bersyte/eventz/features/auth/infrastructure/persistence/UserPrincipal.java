package com.bersyte.eventz.features.auth.infrastructure.persistence;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(UserEntity user) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }
    @Override
    public String getPassword() { return user.getPassword(); }
    @Override
    public String getUsername() { return user.getEmail(); }
}
