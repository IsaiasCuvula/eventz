package com.bersyte.eventz.features.auth.infrastructure.configs;

import com.bersyte.eventz.features.security.config.JwtConfiguration;
import com.bersyte.eventz.features.auth.domain.service.AuthProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthPropertiesAdapter implements AuthProperties, JwtConfiguration {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    
    @Value("${security.jwt.expiration-time}")
    private Duration accessTokenExpiration;
    
    @Value("${refresh.token.expiration-time}")
    private Duration refreshTokenExpiration;
    
    @Value("${auth.verification.code-expiration-minutes}")
    private Duration verificationCodeExpiration;
    
    @Override
    public Duration getVerificationCodeExpiration() {
        return verificationCodeExpiration;
    }
    
    @Override
    public String getJwtSecretKey() {
        return secretKey;
    }
    
    @Override
    public Duration getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
    
    @Override
    public Duration getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
  
}
