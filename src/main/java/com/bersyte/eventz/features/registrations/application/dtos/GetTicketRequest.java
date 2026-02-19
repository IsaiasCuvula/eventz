package com.bersyte.eventz.features.registrations.application.dtos;

import java.util.UUID;

public record GetTicketRequest(
        UUID requesterId, UUID targetUserId, UUID eventId
) {
}
