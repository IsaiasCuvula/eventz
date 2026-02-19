package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.registrations.application.dtos.CancelEventRegistrationRequest;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.application.events.TicketCancellationEvent;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.domain.services.AuditService;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationPublisher;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class CancelEventRegistrationUseCase implements UseCase<CancelEventRegistrationRequest, TicketResponse> {
    private final UserValidationService userValidationService;
    private final EventRegistrationRepository registrationRepository;
    private final EventRegistrationValidationService registrationValidationService;
    private final EventRegistrationMapper eventRegistrationMapper;
    private final EventRepository eventRepository;
    private final Clock clock;
    private final AuditService auditService;
    private final EventRegistrationPublisher eventRegistrationPublisher;
    
    
    public CancelEventRegistrationUseCase(
            UserValidationService userValidationService, EventRegistrationRepository registrationRepository,
            EventRegistrationValidationService registrationValidationService, EventRegistrationMapper eventRegistrationMapper,
            EventRepository eventRepository, Clock clock, AuditService auditService, EventRegistrationPublisher eventRegistrationPublisher
    ) {
        this.userValidationService = userValidationService;
        this.registrationRepository = registrationRepository;
        this.registrationValidationService = registrationValidationService;
        this.eventRegistrationMapper = eventRegistrationMapper;
        this.eventRepository = eventRepository;
        this.clock = clock;
        this.auditService = auditService;
        this.eventRegistrationPublisher = eventRegistrationPublisher;
    }
    
    @Transactional
    @Override
    public TicketResponse execute(CancelEventRegistrationRequest request) {
        UUID requesterId = request.requesterId();
        UUID registrationId = request.registrationId();
        
        AppUser requester = userValidationService.getRequesterById(requesterId);
        EventRegistration registration = registrationValidationService.getValidRegistrationById(registrationId);
        
        LocalDateTime actionDateTime = LocalDateTime.now(clock);
        
        // reverter payment only if follow certain condition (e.g.: week or month before the event)
        EventRegistration cancelledRegistration = registration.cancel(requester, actionDateTime);
        EventRegistration updatedRegistration = registrationRepository.update(cancelledRegistration);
        
        Event targetEvent = updatedRegistration.getEvent();
        
        eventRepository.decrementParticipantCount(targetEvent.getId());
        
        //Send cancellation email
        eventRegistrationPublisher.publishTicketCancellation(
                new TicketCancellationEvent(
                        updatedRegistration.getUser().getEmail(),
                        targetEvent.getTitle(),
                        targetEvent.getDescription(),
                        targetEvent.getDate(),
                        updatedRegistration.getStatus()
                )
        );
        
        auditService.logCancellation(registration, requester, actionDateTime);
        return eventRegistrationMapper.toTicketResponse(updatedRegistration);
    }
}
