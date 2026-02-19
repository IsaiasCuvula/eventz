package com.bersyte.eventz.features.events.application.dtos;

import java.util.UUID;

public record DeleteEventRequest(
        UUID eventId, UUID requesterId
) {
}
