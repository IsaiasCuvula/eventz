package com.bersyte.eventz.features.registrations.application.dtos;

public record GetTicketRequest(
        String requesterId, String targetUserId, String eventId
) {
}
