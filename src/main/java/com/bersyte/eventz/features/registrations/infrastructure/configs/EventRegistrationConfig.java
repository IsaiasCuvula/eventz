package com.bersyte.eventz.features.registrations.infrastructure.configs;

import com.bersyte.eventz.common.domain.services.IdGenerator;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.application.usecases.*;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.domain.services.AuditService;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationPublisher;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationValidationService;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class EventRegistrationConfig {
    
    @Bean
    EventRegistrationMapper eventRegistrationMapper(){
        return new EventRegistrationMapper();
    }
    
    @Bean
    EventRegistrationValidationService eventRegistrationValidationService(
            EventRegistrationRepository registrationRepository
    ){
        return new EventRegistrationValidationService(registrationRepository);
    }
    
    @Bean
    CheckInUseCase checkInUseCase(
            UserValidationService userValidationService,
            EventRegistrationMapper eventRegistrationMapper,
            EventRegistrationRepository eventRegistrationRepository,
            EventRegistrationValidationService eventRegistrationValidationService,
            Clock clock, EventRegistrationPublisher eventRegistrationPublisher
    ){
        return new CheckInUseCase(
                userValidationService, eventRegistrationMapper,
                eventRegistrationRepository, eventRegistrationValidationService, clock,
                eventRegistrationPublisher
        );
    }
    
    @Bean
    FetchEventParticipantsUseCase fetchEventParticipantsUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper, EventValidationService eventValidationService
    ){
        return new  FetchEventParticipantsUseCase(
               eventRegistrationRepository, userValidationService, registrationMapper, eventValidationService);
    }
    
    @Bean
    GetUserValidTicketUseCase getUserTicketUseCase(
            UserValidationService userValidationService,
            EventRegistrationValidationService registrationValidationService,
            EventRegistrationMapper eventRegistrationMapper
    ){
        return new GetUserValidTicketUseCase(
                userValidationService, registrationValidationService, eventRegistrationMapper) ;
    }
    
    @Bean
    JoinEventUseCase joinEventUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            EventValidationService eventValidationService,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper,
            EventRepository eventRepository, IdGenerator idGenerator,
            Clock clock,  EventRegistrationPublisher eventRegistrationPublisher
    ){
        return new JoinEventUseCase(
                eventRegistrationRepository, eventValidationService,
                userValidationService, registrationMapper,
                eventRepository, idGenerator,clock,eventRegistrationPublisher
        ) ;
    }
    
    @Bean
    CancelEventRegistrationUseCase cancelEventRegistrationUseCase(
            UserValidationService userValidationService, EventRegistrationRepository registrationRepository,
            EventRegistrationValidationService registrationValidationService, EventRegistrationMapper eventRegistrationMapper,
            EventRepository eventRepository, Clock clock, AuditService auditService,
            EventRegistrationPublisher eventRegistrationPublisher
    ){
        return new CancelEventRegistrationUseCase(
              userValidationService, registrationRepository, registrationValidationService,
                eventRegistrationMapper, eventRepository, clock, auditService,
                 eventRegistrationPublisher
        );
    }
    
    @Bean
    UpdateTicketCheckInTokenUseCase updateTicketCheckInTokenUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            EventRegistrationValidationService eventRegistrationValidationService,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper,
            IdGenerator idGenerator, Clock clock, AuditService auditService,
            EventRegistrationPublisher eventRegistrationPublisher
    ){
       return new UpdateTicketCheckInTokenUseCase(
               eventRegistrationRepository, eventRegistrationValidationService,
               userValidationService, registrationMapper, idGenerator, clock, auditService,
               eventRegistrationPublisher
       );
    }
    
}
