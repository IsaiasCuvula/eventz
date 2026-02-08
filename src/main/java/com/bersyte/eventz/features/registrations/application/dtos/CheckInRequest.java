package com.bersyte.eventz.features.registrations.application.dtos;

public record CheckInRequest(
        String requesterId, String token, String deviceScannerId
) {
}
