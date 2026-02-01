package com.bersyte.eventz.features.registrations.application.dtos;

public record UpdateTicketCheckInTokenRequest(
        String requesterEmail, String oldToken
) {
}
