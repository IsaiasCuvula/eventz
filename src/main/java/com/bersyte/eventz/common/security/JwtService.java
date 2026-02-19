package com.bersyte.eventz.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    
    private final JwtConfiguration jwtConfig;
    
    public JwtService(JwtConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    
    public boolean isAccessTokenValid(String token) {
        try {
            return isAccessToken(token) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public String generateToken(UUID userId) {
        Map<String, Object> extraClaims = Map.of("type", "access");
        long expirationMs = jwtConfig.getAccessTokenExpiration().toMillis();
        return buildToken(userId, extraClaims, expirationMs);
    }
    
    public String generateRefreshToken(UUID userId) {
        Map<String, Object> extraClaims = Map.of("type", "refresh");
        long expirationMs = jwtConfig.getRefreshTokenExpiration().toMillis();
        return buildToken(userId, extraClaims, expirationMs);
    }
    
    
    public boolean isRefreshToken(String token) {
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return "refresh".equals(type);
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }
    
    
    public boolean isAccessToken(String token) {
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return "access".equals(type);
    }
    
    public String extractTokenId(String token) {
        return extractClaim(token, Claims::getId);
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public LocalDateTime extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return LocalDateTime.ofInstant(
                expiration.toInstant(),
                java.time.ZoneId.systemDefault()
        );
    }
    
    private SecretKey getKey() {
        byte[] encodedKey = Base64.getDecoder().decode(jwtConfig.getJwtSecretKey());
        return Keys.hmacShaKeyFor(encodedKey);
    }
    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
            return Jwts.parser()
                           .verifyWith(getKey())
                           .build()
                           .parseSignedClaims(token)
                           .getPayload();
    }
    
    
    private String buildToken(
            UUID userId, Map<String, Object> claims, long expirationTime
    ) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plusMillis(expirationTime);
            
            return Jwts.builder()
                           .id(java.util.UUID.randomUUID().toString())
                           .claims()
                           .add(claims)
                           .subject(userId.toString())
                           .issuedAt(Date.from(now))
                           .expiration(Date.from(expiry))
                           .and()
                           .signWith(getKey())
                           .compact();
        } catch (Exception e) {
            throw new JwtAuthenticationException("Error while generating token");
        }
    }
    
}