package com.bersyte.eventz.features.registrations.application.dtos;

import java.util.UUID;

public record CancelEventRegistrationRequest(
        UUID registrationId, UUID requesterId
) {
}
