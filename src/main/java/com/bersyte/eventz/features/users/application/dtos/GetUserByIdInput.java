package com.bersyte.eventz.features.users.application.dtos;

public record GetUserByIdInput(
        String requesterId,
        String targetId
) {
}
