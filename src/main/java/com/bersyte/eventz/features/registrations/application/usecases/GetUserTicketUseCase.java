package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.application.dtos.GetTicketRequest;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class GetUserTicketUseCase implements UseCase<GetTicketRequest, TicketResponse> {
    private final UserValidationService userValidationService;
    private final EventRegistrationValidationService registrationValidationService;
    private final EventRegistrationMapper eventRegistrationMapper;
    
    public GetUserTicketUseCase(
            UserValidationService userValidationService,
            EventRegistrationValidationService registrationValidationService,
            EventRegistrationMapper eventRegistrationMapper
    ) {
        this.userValidationService = userValidationService;
        this.registrationValidationService = registrationValidationService;
        this.eventRegistrationMapper = eventRegistrationMapper;
    }
    
    @Override
    public TicketResponse execute(GetTicketRequest request) {
        AppUser requester = userValidationService.getRequester(request.requesterEmail());
       
        EventRegistration registration = registrationValidationService.getValidTicketByEvent(
                request.eventId(),request.userId()
        );
        
        if(!registration.canManage(requester)){
            throw new UnauthorizedException("You don't have permission");
        }
        
        return eventRegistrationMapper.toTicketResponse(registration);
    }
}