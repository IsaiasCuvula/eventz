package com.bersyte.eventz.features.users.application.dtos;

public record DeleteUserRequest(
        String requesterEmail,
        String userId
) {
}
