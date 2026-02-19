package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.common.domain.dtos.Pagination;

import java.util.UUID;

public record EventsByOrganizerRequest(
        Pagination pagination, UUID organizerId
        
) {
}
