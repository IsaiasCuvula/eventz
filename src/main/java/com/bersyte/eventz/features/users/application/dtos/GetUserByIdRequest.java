package com.bersyte.eventz.features.users.application.dtos;

import java.util.UUID;

public record GetUserByIdRequest(
        UUID requesterId,
        UUID targetId
) {
}
