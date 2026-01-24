package com.bersyte.eventz.features.events.application.dtos;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public record EventResponse(
    String id,
    String title,
    String description,
    String location,
    String organizer,
    LocalDateTime date,
    List<String> participants,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
