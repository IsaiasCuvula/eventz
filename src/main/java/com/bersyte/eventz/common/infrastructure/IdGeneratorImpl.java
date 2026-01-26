package com.bersyte.eventz.common.infrastructure;

import com.bersyte.eventz.common.domain.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorImpl implements IdGenerator {
    @Override
    public String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
