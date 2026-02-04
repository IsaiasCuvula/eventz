package com.bersyte.eventz.features.auth.infrastructure.security;

import com.bersyte.eventz.common.security.JwtService;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JwtTokenServiceAdapter implements TokenService {
    private final JwtService jwtService;
    
    public JwtTokenServiceAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    @Override
    public String refreshToken(String oldToken) {
        return jwtService.generateRefreshToken(oldToken);
    }
    
    @Override
    public TokenPair createUserTokens(String email) {
        String token = jwtService.generateToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        LocalDateTime expiration = jwtService.extractExpiration(token);
        return new TokenPair(token, refreshToken, expiration);
    }
}
