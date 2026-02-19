package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.features.events.domain.model.EventAccessType;

import java.time.LocalDateTime;
import java.util.UUID;


public record EventResponse(
    UUID id,
    String title,
    String description,
    String location,
    LocalDateTime date,
    Integer participantsCount,
    EventOrganizer organizer,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    EventAccessType accessType,
    Integer price
) {}
