package com.bersyte.eventz.features.users.application.dtos;


import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
