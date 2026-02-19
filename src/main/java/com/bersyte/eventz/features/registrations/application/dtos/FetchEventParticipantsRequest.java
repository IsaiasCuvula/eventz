package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.common.domain.dtos.Pagination;

import java.util.UUID;

public record FetchEventParticipantsRequest(
        UUID eventId, UUID requesterId,
        Pagination pagination
) {
}
