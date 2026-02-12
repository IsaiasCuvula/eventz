package com.bersyte.eventz.features.registrations.infrastructure.configs;

import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.registrations.domain.repository.RegistrationAuditLogRepository;
import com.bersyte.eventz.features.registrations.domain.services.AuditService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfig {
    
    @Bean
    AuditService auditService(
            RegistrationAuditLogRepository auditLogRepository,
            IdGenerator idGenerator
    ){
        return new AuditService(auditLogRepository, idGenerator);
    }
    
}
