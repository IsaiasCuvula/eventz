package com.bersyte.eventz.features.security.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class JwtAuthenticationException extends DomainException {
    public JwtAuthenticationException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
