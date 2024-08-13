package com.bersyte.eventz.security.auth;

import java.util.Date;

public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(AppUser entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getCreatedAt(),
                entity.getRole()
        );
    }

    public static AppUser toUserEntity(UserRequestDTO dto) {
        AppUser entity = new AppUser();
        entity.setUsername(dto.username());
        entity.setPassword(dto.password());
        entity.setRole(dto.role());
        entity.setCreatedAt(new Date(dto.createdAt()));
        return entity;
    }
}
