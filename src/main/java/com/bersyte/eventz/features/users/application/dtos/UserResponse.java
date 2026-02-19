package com.bersyte.eventz.features.users.application.dtos;


import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
