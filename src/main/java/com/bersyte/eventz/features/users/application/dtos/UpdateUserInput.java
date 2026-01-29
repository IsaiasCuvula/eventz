package com.bersyte.eventz.features.users.application.dtos;


public record UpdateUserInput(
        UpdateUserRequest request,
        String userId,
        String email
) {
}
