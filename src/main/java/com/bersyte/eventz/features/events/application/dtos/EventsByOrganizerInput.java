package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.common.domain.Pagination;

public record EventsByOrganizerInput(
        Pagination pagination,String organizerId
        
) {
}
