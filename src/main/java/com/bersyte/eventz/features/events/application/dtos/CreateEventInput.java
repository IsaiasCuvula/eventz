package com.bersyte.eventz.features.events.application.dtos;

import java.util.UUID;

public record CreateEventInput(
        UUID requesterId,
        CreateEventRequest request
) {}
