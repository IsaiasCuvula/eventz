package com.bersyte.eventz.features.users.domain.exception;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }
}
