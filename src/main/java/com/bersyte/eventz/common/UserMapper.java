package com.bersyte.eventz.common;

import com.bersyte.eventz.auth.RegisterRequestDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper {

    public AppUser toUserEntity(RegisterRequestDto dto) {
        if (dto == null) {
            throw new NullPointerException ("Register dto cannot be null");
        }
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
        if (entity == null) {
            throw new NullPointerException ("User cannot be null");
        }
        //
        return new UserResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName (),
                entity.getPhone (),
                entity.getCreatedAt(),
                entity.getRole()
        );
    }
}
