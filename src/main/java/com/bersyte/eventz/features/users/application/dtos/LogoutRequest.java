package com.bersyte.eventz.features.users.application.dtos;

public record LogoutRequest(
        String refreshToken
) {
}
