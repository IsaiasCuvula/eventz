package com.bersyte.eventz.features.auth.infrastructure.persistence.mappers;

import com.bersyte.eventz.features.auth.domain.model.RefreshToken;
import com.bersyte.eventz.features.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenEntityMapper {
    
    public RefreshToken toDomain(RefreshTokenEntity entity){
        return  new RefreshToken(
                entity.getTokenId(),
                entity.getUser().getId(),
                entity.getExpiresAt()
        );
    }
    
    public RefreshTokenEntity toEntity(RefreshToken token, UserEntity entity){
        return new RefreshTokenEntity(
                token.tokenId(),
                entity,
                token.expiresAt()
        );
    }
}
