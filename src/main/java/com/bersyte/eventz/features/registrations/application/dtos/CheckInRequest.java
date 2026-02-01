package com.bersyte.eventz.features.registrations.application.dtos;

public record CheckInRequest(
        String requesterEmail, String token, String deviceScannerId
) {
}
