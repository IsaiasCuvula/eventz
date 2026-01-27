package com.bersyte.eventz.features.events.application.dtos;

import java.time.LocalDateTime;


public record EventResponse(
    String id,
    String title,
    String description,
    String location,
    LocalDateTime date,
    Integer participantsCount,
    EventOrganizer organizer,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
