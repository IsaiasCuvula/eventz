package com.bersyte.eventz.features.registrations.application.dtos;

import java.util.UUID;

public record CheckInRequest(
        UUID requesterId, String token, String deviceScannerId
) {
}
