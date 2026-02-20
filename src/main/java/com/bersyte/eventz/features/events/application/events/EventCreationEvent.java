package com.bersyte.eventz.features.events.application.events;

import com.bersyte.eventz.features.events.domain.model.EventAccessType;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventCreationEvent(
        UUID id,
        String title,
        String description,
        String location,
        LocalDateTime date,
        EventAccessType accessType,
        Integer price
) {
}
