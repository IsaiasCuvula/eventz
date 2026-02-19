package com.bersyte.eventz.features.users.application.dtos;


import java.util.UUID;

public record UpdateUserInput(
        UpdateUserRequest request,
        UUID targetUserId,
        UUID requesterId
) {
}
