package com.bersyte.eventz.features.registrations.application.dtos;

public record UpdateTicketStatusRequest(
        String requesterId, String ticketId
) {
}
