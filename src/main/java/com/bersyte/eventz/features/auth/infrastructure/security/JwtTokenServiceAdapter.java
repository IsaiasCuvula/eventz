package com.bersyte.eventz.features.auth.infrastructure.security;

import com.bersyte.eventz.common.security.JwtAuthenticationException;
import com.bersyte.eventz.common.security.JwtService;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.RefreshToken;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.auth.infrastructure.persistence.adapters.RefreshTokenPersistenceAdapter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class JwtTokenServiceAdapter implements TokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    
    public JwtTokenServiceAdapter(JwtService jwtService, RefreshTokenPersistenceAdapter refreshTokenRepository) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    @Override
    public UUID validateRefreshToken(String refreshToken) {
        boolean isRefreshToken = jwtService.isRefreshToken(refreshToken);
        if(!isRefreshToken){
            throw new JwtAuthenticationException("Invalid token");
        }
        if(jwtService.isTokenExpired(refreshToken)){
            throw new JwtAuthenticationException("The token has expired. Please log in again.");
        }
        
        UUID extractedUserId = UUID.fromString(jwtService.extractUserId(refreshToken));
        String tokenId = jwtService.extractTokenId(refreshToken);
        
        RefreshToken savedToken =refreshTokenRepository.getTokenById(tokenId)
                                 .orElseThrow(()-> new JwtAuthenticationException(("Token has been revoked")));
        
        if (!savedToken.userId().equals(extractedUserId)) {
            throw new JwtAuthenticationException("Token identity mismatch");
        }
        
        return extractedUserId;
    }
    
    @Override
    public TokenPair createAndPersistTokens(AuthUser user) {
        TokenPair tokens = createUserTokens(user.userId());
        String tokenId = extractTokenId(tokens.refreshToken());
        LocalDateTime expiresAt = extractExpiration(tokens.refreshToken());
        RefreshToken refreshToken = new RefreshToken(tokenId, user.userId(), expiresAt);
        refreshTokenRepository.saveToken(refreshToken);
        return tokens;
    }
    
   
    
    @Override
    public String extractTokenId(String token) {
        return jwtService.extractTokenId(token);
    }
    
    @Override
    public LocalDateTime extractExpiration(String token) {
        return jwtService.extractExpiration(token);
    }
    
    @Override
    public void invalidateToken(String targetToken) {
        String tokenId = extractTokenId(targetToken);
        refreshTokenRepository.deleteByTokenId(tokenId);
    }
    
    private TokenPair createUserTokens(UUID userId) {
        String token = jwtService.generateToken(userId);
        String refreshToken = jwtService.generateRefreshToken(userId);
        LocalDateTime expiration = jwtService.extractExpiration(token);
        return new TokenPair(token, refreshToken, expiration);
    }
}
