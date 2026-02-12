package com.bersyte.eventz.features.auth.infrastructure.persistence.entities;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    private String tokenId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity user;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
}
