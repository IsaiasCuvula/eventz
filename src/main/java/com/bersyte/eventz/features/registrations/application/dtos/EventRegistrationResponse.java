package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;

import java.time.LocalDateTime;

public record EventRegistrationResponse(
        String id,
        UserResponse user,
        EventResponse event,
        RegistrationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
