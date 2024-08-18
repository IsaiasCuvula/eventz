package com.bersyte.eventz.common;

import com.bersyte.eventz.security.auth.RegisterRequestDTO;

import java.util.Date;

public class UserMapper {

    public static AppUser toUserEntity(RegisterRequestDTO dto) {
        AppUser entity = new AppUser();
        entity.setEmail(dto.email());
        entity.setPassword(dto.password());
        entity.setRole(dto.role());
        entity.setPhone(dto.phone());
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setCreatedAt(new Date(dto.createdAt()));
        return entity;
    }


    public static UserResponseDTO toUserResponseDTO(AppUser entity) {
        final String phone = entity.getPhone() != null ? entity.getPhone() : " ";
        final String lastName = entity.getLastName() != null ? entity.getLastName() : " ";
        //
        return new UserResponseDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                lastName,
                phone,
                entity.getCreatedAt(),
                entity.getRole()
        );
    }
}
