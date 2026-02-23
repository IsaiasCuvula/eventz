package com.bersyte.eventz.features.auth.application.dtos;

import java.util.UUID;

public record RevokeAllTokensRequest(
        UUID requesterId,UUID targetId
) {
}
