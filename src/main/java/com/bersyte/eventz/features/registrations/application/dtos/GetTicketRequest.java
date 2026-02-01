package com.bersyte.eventz.features.registrations.application.dtos;

public record GetTicketRequest(
        String requesterEmail, String eventId, String userId
) {
}
