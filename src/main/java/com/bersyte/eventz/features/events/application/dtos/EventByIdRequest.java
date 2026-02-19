package com.bersyte.eventz.features.events.application.dtos;

import java.util.UUID;

public record EventByIdRequest(
        UUID eventId
) {
}
