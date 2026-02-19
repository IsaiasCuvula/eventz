package com.bersyte.eventz.features.registrations.application.dtos;

import java.util.UUID;

public record EventRegistrationRequest(
        UUID eventId, UUID targetUserId, UUID requesterId
) {
}
