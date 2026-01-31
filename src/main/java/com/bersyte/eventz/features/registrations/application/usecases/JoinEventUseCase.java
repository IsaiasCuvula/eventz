package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.common.domain.exceptions.BusinessException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationRequest;
import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationResponse;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationAlreadyExistsException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;


public class JoinEventUseCase implements UseCase<EventRegistrationRequest, EventRegistrationResponse> {
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventValidationService eventValidationService;
    private final UserValidationService userValidationService;
    private final EventRegistrationMapper registrationMapper;
    private final EventRepository eventRepository;
    private final IdGenerator idGenerator;
    
    
    public JoinEventUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            EventValidationService eventValidationService,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper,
            EventRepository eventRepository, IdGenerator idGenerator
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventValidationService = eventValidationService;
        this.userValidationService = userValidationService;
        this.registrationMapper = registrationMapper;
        this.eventRepository = eventRepository;
        this.idGenerator = idGenerator;
    }
    
    @Transactional
    @Override
    public EventRegistrationResponse execute(EventRegistrationRequest request) {
        String eventId = request.eventId();
        String userId = request.userId();
        boolean registered= eventRegistrationRepository.alreadyRegistered(eventId, userId);
        if(registered){
            throw new EventRegistrationAlreadyExistsException(eventId);
        }
        
        Event event = eventValidationService.getValidEventById(eventId);
        AppUser user = userValidationService.getValidUserById(userId);
        
        if(!event.canAcceptMoreParticipants()){
            throw new BusinessException("The event is already full");
        }
        String registrationId = idGenerator.generateUuid();
        EventRegistration registration = registrationMapper.toDomain(event, user,registrationId);
        
        //Payment can be handled here ... before registration
        
        EventRegistration savedRegistration = eventRegistrationRepository.joinEvent(registration);
        eventRepository.incrementParticipantCount(eventId);
        
        //Email will be handled here ....
        
        return registrationMapper.toResponse(savedRegistration);
    }
}
