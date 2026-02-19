package com.bersyte.eventz.common.domain.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s with Id: %s not found", resourceName, id));
    }
}
