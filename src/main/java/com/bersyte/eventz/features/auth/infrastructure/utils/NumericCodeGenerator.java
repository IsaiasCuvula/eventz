package com.bersyte.eventz.features.auth.infrastructure.utils;


import com.bersyte.eventz.features.auth.domain.service.CodeGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class NumericCodeGenerator implements CodeGenerator {
   
    @Override
    public String generate() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(1000000);
        return String.format("%06d", code);
    }
}
