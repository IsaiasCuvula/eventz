package com.bersyte.eventz.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    
    private final JwtConfiguration jwtConfig;
    
    public JwtService(JwtConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    
    public String refreshAccessToken(String oldRefreshToken) {
        if (!isRefreshToken(oldRefreshToken)) {
            throw new JwtException("Not a refresh token");
        }
        
        if (isTokenExpired(oldRefreshToken)) {
            throw new JwtException("Refresh token expired - LOGIN REQUIRED");
        }
        
        String username = extractUsername(oldRefreshToken);
        return generateToken(username);
    }
    
    public String generateToken(String username) {
        Map<String, Object> extraClaims = Map.of("type", "access");
        long expirationMs = jwtConfig.getAccessTokenExpiration().toMillis();
        return buildToken(username, extraClaims, expirationMs);
    }
    
    public String generateRefreshToken(String username) {
        Map<String, Object> extraClaims = Map.of("type", "refresh");
        long expirationMs = jwtConfig.getRefreshTokenExpiration().toMillis();
        return buildToken(username, extraClaims, expirationMs);
    }
    
    
    
    public boolean isRefreshToken(String token) {
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return "refresh".equals(type);
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    
    public boolean isAccessToken(String token) {
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return "access".equals(type);
    }
    
    public String extractUsername(String token) {
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
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }
    
    private String buildToken(
            String username,
            Map<String, Object> claims,
            long expirationTime
    ) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plusMillis(expirationTime);
            
            return Jwts.builder()
                           .claims()
                           .add(claims)
                           .subject(username)
                           .issuedAt(Date.from(now))
                           .expiration(Date.from(expiry))
                           .and()
                           .signWith(getKey())
                           .compact();
        } catch (JwtException e) {
            throw new JwtException("Error while generating token", e);
        }
    }
    
}