package com.bersyte.eventz.features.auth.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
            super("Invalid email or password.", ErrorCode.INVALID_CREDENTIALS);
    }
}
