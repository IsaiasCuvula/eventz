package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.registrations.application.dtos.CheckInRequest;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.application.events.CheckinEvent;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationPublisher;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

public class CheckInUseCase implements UseCase<CheckInRequest, TicketResponse> {
    private final UserValidationService userValidationService;
    private final EventRegistrationMapper eventRegistrationMapper;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRegistrationValidationService eventRegistrationValidationService;
    private final Clock clock;
    private final EventRegistrationPublisher eventRegistrationPublisher;
    
    public CheckInUseCase(
            UserValidationService userValidationService,
            EventRegistrationMapper eventRegistrationMapper,
            EventRegistrationRepository eventRegistrationRepository,
            EventRegistrationValidationService eventRegistrationValidationService,
            Clock clock, EventRegistrationPublisher eventRegistrationPublisher
    ) {
        this.userValidationService = userValidationService;
        this.eventRegistrationMapper = eventRegistrationMapper;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRegistrationValidationService = eventRegistrationValidationService;
        this.clock = clock;
        this.eventRegistrationPublisher = eventRegistrationPublisher;
    }
    
    @Transactional
    @Override
    public TicketResponse execute(CheckInRequest request) {
        AppUser requester = userValidationService.getRequesterById(request.requesterId());
        
        if(!requester.canManageEvents()){
            throw new UnauthorizedException("You dont have permission");
        }
        
        EventRegistration registration = eventRegistrationValidationService.getValidRegistrationByCheckInToken(
                request.token()
        );
        
        if(!registration.canCheckIn(requester)){
            throw new UnauthorizedException("You are not authorized to check-in for this event");
        }
        LocalDateTime checkInDateTime = LocalDateTime.now(clock);
        EventRegistration updatedRegistration = registration.markAsUsed(requester,checkInDateTime);
        EventRegistration savedRegistration = eventRegistrationRepository.update(updatedRegistration);
        
        //sent email to the user about the check in ...
        eventRegistrationPublisher.publishCheckin(
                new CheckinEvent(
                        savedRegistration.getUser().getEmail(),
                        savedRegistration.getEvent().getTitle(),
                        savedRegistration.getEvent().getLocation(),
                        checkInDateTime
                )
        );
        
        return eventRegistrationMapper.toTicketResponse(savedRegistration);
    }
}
