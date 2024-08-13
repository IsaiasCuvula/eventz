package com.bersyte.eventz.security.auth;

import java.util.Date;

public class AuthMapper {

    public static AuthUser toAuthUser(RegisterRequestDTO dto) {
        AuthUser entity = new AuthUser();
        entity.setUsername(dto.username());
        entity.setPassword(dto.password());
        entity.setRole(dto.role());
        entity.setCreatedAt(new Date(dto.createdAt()));
        return entity;
    }


    public static AuthResponse toAuthResponse(AuthUser entity, String token) {
        return new AuthResponse(
                token,
                entity.getId(),
                entity.getUsername(),
                entity.getCreatedAt(),
                entity.getRole()
        );
    }
}
