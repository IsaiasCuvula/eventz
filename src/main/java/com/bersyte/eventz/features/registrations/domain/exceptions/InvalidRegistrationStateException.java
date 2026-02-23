package com.bersyte.eventz.features.registrations.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class InvalidRegistrationStateException extends DomainException {
    public InvalidRegistrationStateException(String message) {
        super(message,  ErrorCode.REGISTRATION_INVALID_STATE);
    }
}
