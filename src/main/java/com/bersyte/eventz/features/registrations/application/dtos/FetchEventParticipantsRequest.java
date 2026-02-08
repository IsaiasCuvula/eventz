package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.common.domain.Pagination;

public record FetchEventParticipantsRequest(
        String eventId, String requesterId,
        Pagination pagination
) {
}
