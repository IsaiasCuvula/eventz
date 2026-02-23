package com.bersyte.eventz.common.presentation.dtos;

import java.time.LocalDateTime;

public record ErrorResponse(
        String path,
        String message,
        String errorCode,
        int statusCode,
        LocalDateTime timestamp
) {
}