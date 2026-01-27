package com.bersyte.eventz.features.events.infrastructure.config;

import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.application.usecases.*;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;

@Configuration
@EnableJpaAuditing // Enables date monitoring
public class EventBeanConfig {
    
    @Bean
    EventMapper  eventMapper(){
        return new EventMapper();
    }
    
    @Bean
    EventValidationService eventValidationService(EventRepository repository){
        return new EventValidationService(repository);
    }
    
    @Bean
    CreateEventUseCase createEventUseCase(
            EventRepository repository,
            UserValidationService userValidationService,
            EventMapper mapper, IdGenerator idGenerator
    ) {
        return new CreateEventUseCase(
                repository, userValidationService, mapper, idGenerator);
    }
    
    @Bean
    DeleteEventUseCase deleteEventUseCase(
            EventRepository repository,
            UserValidationService userValidationService,
            EventValidationService eventValidationService
    ){
        return new DeleteEventUseCase(
                repository, userValidationService,
                eventValidationService
        );
    }
    
    @Bean
    FetchEventsByOrganizerUseCase  fetchEventsByOrganizerUseCase(
            EventRepository eventRepository,
            EventMapper eventMapper,
            UserValidationService userValidationService
    ) {
        return new FetchEventsByOrganizerUseCase(
                eventRepository, eventMapper,
                userValidationService);
    }
    
    @Bean
    FetchEventsUseCase fetchEventsUseCase(
            EventRepository repository, EventMapper mapper
    ) {
        return new FetchEventsUseCase(repository, mapper);
    }
    
    @Bean
    FetchUpcomingEventsUseCase fetchUpcomingEventsUseCase(
            EventRepository repository, EventMapper mapper,
            Clock clock
    ) {
        return new FetchUpcomingEventsUseCase(repository,mapper,clock);
    }
    
    @Bean
    FilterEventsUseCase filterEventsUseCase(
            EventRepository repository, EventMapper mapper
    ){
        return new FilterEventsUseCase(repository, mapper);
    }
    
    @Bean
    GetEventsByDateUseCase getEventsByDateUseCase(
            EventRepository repository, EventMapper mapper
    ){
        return new GetEventsByDateUseCase(repository, mapper);
    }
    
    @Bean
    UpdateEventUseCase updateEventUseCase(
            EventRepository repository, EventMapper mapper,
            UserValidationService userValidationService,
            EventValidationService eventValidationService
    ){
        return new UpdateEventUseCase(repository, mapper,
                userValidationService, eventValidationService);
    }
}
