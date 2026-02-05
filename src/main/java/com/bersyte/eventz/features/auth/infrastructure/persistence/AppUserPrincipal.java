package com.bersyte.eventz.features.auth.infrastructure.persistence;

import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AppUserPrincipal(UserEntity userEntity) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name().toUpperCase())
        );
    }
    @Override
    public String getPassword() { return userEntity.getPassword(); }
    @Override
    public String getUsername() { return userEntity.getEmail(); }
    
    public UserEntity getUserEntity() { return userEntity; }
}
