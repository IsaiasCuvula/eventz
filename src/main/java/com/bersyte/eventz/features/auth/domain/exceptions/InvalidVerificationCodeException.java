package com.bersyte.eventz.features.auth.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class InvalidVerificationCodeException  extends DomainException {
    
    public InvalidVerificationCodeException(String message) {
        
        super(message, ErrorCode.INTERNAL_VERIFICATION_CODE);
    }
}