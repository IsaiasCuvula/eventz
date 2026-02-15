package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.application.dtos.UpdateTicketCheckInTokenRequest;
import com.bersyte.eventz.features.registrations.application.events.UpdateCheckinTokenEvent;
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

public class UpdateTicketCheckInTokenUseCase implements UseCase<UpdateTicketCheckInTokenRequest, TicketResponse> {
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRegistrationValidationService eventRegistrationValidationService;
    private final UserValidationService userValidationService;
    private final EventRegistrationMapper registrationMapper;
    private final IdGenerator idGenerator;
    private final Clock clock;
    private final AuditService auditService;
    private final EventRegistrationPublisher eventRegistrationPublisher;
    
    
    public UpdateTicketCheckInTokenUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            EventRegistrationValidationService eventRegistrationValidationService,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper,
            IdGenerator idGenerator, Clock clock, AuditService auditService, EventRegistrationPublisher eventRegistrationPublisher
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRegistrationValidationService = eventRegistrationValidationService;
        this.userValidationService = userValidationService;
        this.registrationMapper = registrationMapper;
        this.idGenerator = idGenerator;
        this.clock = clock;
        this.auditService = auditService;
        this.eventRegistrationPublisher = eventRegistrationPublisher;
    }
    
    @Transactional
    @Override
    public TicketResponse execute(UpdateTicketCheckInTokenRequest request) {
        String requesterId = request.requesterId();
        String oldToken = request.oldToken();
        AppUser requester = userValidationService.getRequesterById(requesterId);
        
        if(!requester.canManageEvents()){
            throw new UnauthorizedException("You don't have permission");
        }
        EventRegistration registration = eventRegistrationValidationService.getValidRegistrationByCheckInToken(
                oldToken
        );
        
        LocalDateTime actionDateTime = LocalDateTime.now(clock);
        
        String newCheckInToken = idGenerator.generateCheckInToken();
        EventRegistration updatedRegistration =registration.updateCheckInToken(
                newCheckInToken, requester, actionDateTime
        );
        
        EventRegistration savedRegistration = eventRegistrationRepository.update(updatedRegistration);
        auditService.logTokenRotation(
                registration.getId(), requester.getId(), requester.getFullName(),
                oldToken, newCheckInToken, actionDateTime
        );
        
        eventRegistrationPublisher.publishUpdateCheckinToken(
                new UpdateCheckinTokenEvent(
                        savedRegistration.getUser().getEmail(),
                        savedRegistration.getEvent().getTitle(),
                        newCheckInToken
                )
        );
        
        return registrationMapper.toTicketResponse(savedRegistration);
    }
}

