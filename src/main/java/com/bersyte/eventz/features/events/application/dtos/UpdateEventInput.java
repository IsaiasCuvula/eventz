package com.bersyte.eventz.features.events.application.dtos;

import java.util.UUID;

public record UpdateEventInput(
        UpdateEventRequest request,
        UUID eventId,
        UUID requesterId
) {
}
