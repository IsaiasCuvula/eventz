package com.bersyte.eventz.common.domain.services;

import java.util.UUID;

public interface IdGenerator {
    UUID generateUuid();
    String generateCheckInToken();
}
