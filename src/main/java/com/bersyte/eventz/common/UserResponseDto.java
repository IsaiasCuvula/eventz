package com.bersyte.eventz.common;

import java.util.Date;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        Date createdAt,
        UserRole role
) {
}
