package com.bersyte.eventz.common;

import com.bersyte.eventz.auth.RegisterRequestDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper {

    public AppUser toUserEntity(RegisterRequestDto dto) {
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


    public UserResponseDto toUserResponseDTO(AppUser entity) {
        final String phone = entity.getPhone() != null ? entity.getPhone() : " ";
        final String lastName = entity.getLastName() != null ? entity.getLastName() : " ";
        //
        return new UserResponseDto(
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
