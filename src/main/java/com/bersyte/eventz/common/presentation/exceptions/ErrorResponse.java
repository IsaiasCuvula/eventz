package com.bersyte.eventz.common.presentation.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        String path,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}