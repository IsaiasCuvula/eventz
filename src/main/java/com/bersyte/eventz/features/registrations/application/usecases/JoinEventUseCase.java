package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.common.domain.exceptions.BusinessException;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationRequest;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.application.events.EventRegistrationEvent;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationAlreadyExistsException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationPublisher;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

public class JoinEventUseCase implements UseCase<EventRegistrationRequest, TicketResponse> {
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventValidationService eventValidationService;
    private final UserValidationService userValidationService;
    private final EventRegistrationMapper registrationMapper;
    private final EventRepository eventRepository;
    private final IdGenerator idGenerator;
    private final Clock clock;
    private final EventRegistrationPublisher eventRegistrationPublisher;
    
    
    public JoinEventUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            EventValidationService eventValidationService,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper,
            EventRepository eventRepository, IdGenerator idGenerator, Clock clock, EventRegistrationPublisher eventRegistrationPublisher
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventValidationService = eventValidationService;
        this.userValidationService = userValidationService;
        this.registrationMapper = registrationMapper;
        this.eventRepository = eventRepository;
        this.idGenerator = idGenerator;
        this.clock = clock;
        this.eventRegistrationPublisher = eventRegistrationPublisher;
    }
    
    @Transactional
    @Override
    public TicketResponse execute(EventRegistrationRequest request) {
        String requesterId = request.requesterId();
        String targetId = request.targetUserId();
        String eventId = request.eventId();
        
        AppUser requester = userValidationService.getRequesterById(requesterId);
        
        Event event = eventValidationService.getValidEventById(eventId);
        
        if(!event.canAcceptMoreParticipants()){
            throw new BusinessException("The event is already full");
        }
       
        boolean registered = eventRegistrationRepository.alreadyRegistered(
                eventId, targetId, EventRegistration.BLOCKING_STATUSES
        );
        
        if(registered){
            throw new EventRegistrationAlreadyExistsException(eventId);
        }
        
        AppUser target;
        String registrationId = idGenerator.generateUuid();
        String checkInToken = idGenerator.generateCheckInToken();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        
        if(requester.getId().equals(targetId)){
            target = requester;
        }else{
            if(!requester.canManageEvents()){
                throw new UnauthorizedException("You do not have permission");
            }
            target = userValidationService.getValidUserById(targetId);
        }
        
        EventRegistration registration = EventRegistration.create(
                registrationId, checkInToken, event, target,
                createdAt
        );
       
        //Payment can be handled here ... before registration
        
        EventRegistration savedRegistration = eventRegistrationRepository.joinEvent(registration);
        eventRepository.incrementParticipantCount(eventId);
        
        //Email will be handled here ....
        eventRegistrationPublisher.publishJoinEvent(
                new EventRegistrationEvent(
                        target.getEmail(),
                        target.getFullName(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getDate(),
                        savedRegistration.getCheckInToken(),
                        savedRegistration.getStatus()
                )
        );
        
        return registrationMapper.toTicketResponse(savedRegistration);
    }
}
