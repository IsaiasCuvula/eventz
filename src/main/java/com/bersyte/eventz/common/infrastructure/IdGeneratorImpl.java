package com.bersyte.eventz.common.infrastructure;

import com.bersyte.eventz.common.domain.services.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorImpl implements IdGenerator {
    @Override
    public UUID generateUuid() {
        return getRandomUUID();
    }
    
    @Override
    public String generateCheckInToken() {
        return "TKT_" + getRandomUUID().toString().replace("-", "");
    }
    
    private UUID getRandomUUID(){
        return UUID.randomUUID();
    }
}
