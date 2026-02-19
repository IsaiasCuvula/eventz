package com.bersyte.eventz.features.auth.infrastructure.persistence.adapters;

import com.bersyte.eventz.features.auth.domain.model.RefreshToken;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import com.bersyte.eventz.features.auth.infrastructure.persistence.mappers.RefreshTokenEntityMapper;
import com.bersyte.eventz.features.auth.infrastructure.persistence.repositories.RefreshTokenJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository tokenJpaRepository;
    private final RefreshTokenEntityMapper entityMapper;
    private final UserJpaRepository userJpaRepository;
    
    public RefreshTokenPersistenceAdapter(
            RefreshTokenJpaRepository tokenJpaRepository,
            RefreshTokenEntityMapper entityMapper, UserJpaRepository userJpaRepository) {
        this.tokenJpaRepository = tokenJpaRepository;
        this.entityMapper = entityMapper;
        this.userJpaRepository = userJpaRepository;
    }
    
    @Override
    public void deleteByTokenId(String tokenId) {
        tokenJpaRepository.deleteById(tokenId);
    }
    
    @Override
    public void saveToken(RefreshToken token) {
        UserEntity userProxy = userJpaRepository.getReferenceById(token.userId());
        RefreshTokenEntity entity = entityMapper.toEntity(token, userProxy);
        RefreshTokenEntity savedToken = tokenJpaRepository.save(entity);
        entityMapper.toDomain(savedToken);
    }
    
    @Override
    public void revokeAllSessions(UUID userId) {
        tokenJpaRepository.deleteAllByUserId(userId);
    }
    
    @Override
    public Optional<RefreshToken> getTokenById(String tokenId) {
        return tokenJpaRepository.findById(tokenId)
                       .map(entityMapper::toDomain);
       
    }
}
