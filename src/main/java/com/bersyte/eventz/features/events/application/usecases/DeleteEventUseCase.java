package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.application.dtos.DeleteEventRequest;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.util.UUID;

public class DeleteEventUseCase implements VoidUseCase<DeleteEventRequest> {
    private final EventRepository repository;
    private  final UserValidationService userValidationService;
    private final EventValidationService eventValidationService;
    
    public DeleteEventUseCase(EventRepository repository, UserValidationService userValidationService, EventValidationService eventValidationService) {
        this.repository = repository;
        this.userValidationService = userValidationService;
        this.eventValidationService = eventValidationService;
    }
    
    @Override
    public void execute(DeleteEventRequest request) {
        UUID requesterId = request.requesterId();
        UUID eventId = request.eventId();
        AppUser requester = userValidationService.getAuthorizedOrganizerById(requesterId);
        Event event = eventValidationService.getValidEventById(eventId);
        if(!event.canManage(requester)){
            throw new UnauthorizedException("You cannot cancel an event you don't own.");
        }
        repository.deleteEvent(eventId);
    }
}
