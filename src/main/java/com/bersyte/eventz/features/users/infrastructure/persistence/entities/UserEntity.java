package com.bersyte.eventz.features.users.infrastructure.persistence.entities;

import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "organizer")
    private List<EventEntity> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRegistrationEntity> registrations;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationExpiration;

    private boolean enabled;
    
    @LastModifiedDate
    private LocalDateTime updateAt;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String ROLE_PREFIX = "ROLE_";
        UserRole role = this.getRole();

        if (role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority(UserRole.ADMIN.name().toUpperCase()),
                    new SimpleGrantedAuthority(ROLE_PREFIX + role.name().toUpperCase())
            );
        }
        return List.of(new SimpleGrantedAuthority(role.name().toUpperCase()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
