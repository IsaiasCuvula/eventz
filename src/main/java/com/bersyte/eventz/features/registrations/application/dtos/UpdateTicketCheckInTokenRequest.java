package com.bersyte.eventz.features.registrations.application.dtos;

import java.util.UUID;

public record UpdateTicketCheckInTokenRequest(
        UUID requesterId, String oldToken
) {
}
