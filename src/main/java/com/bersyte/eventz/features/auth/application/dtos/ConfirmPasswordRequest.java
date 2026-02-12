package com.bersyte.eventz.features.auth.application.dtos;

public record ConfirmPasswordRequest(
        String email, String recoveryCode, String newPassword
) {
}
