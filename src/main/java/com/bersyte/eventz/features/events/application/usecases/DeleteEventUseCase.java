package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.application.dtos.DeleteEventInput;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class DeleteEventUseCase implements VoidUseCase<DeleteEventInput> {
    private final EventRepository repository;
    private  final UserValidationService userValidationService;
    private final EventValidationService eventValidationService;
    
    public DeleteEventUseCase(EventRepository repository, UserValidationService userValidationService, EventValidationService eventValidationService) {
        this.repository = repository;
        this.userValidationService = userValidationService;
        this.eventValidationService = eventValidationService;
    }
    
    @Override
    public void execute(DeleteEventInput input) {
        String userEmail = input.userEmail();
        String eventId = input.eventId();
        
        AppUser user = userValidationService.getValidUserByEmail(userEmail);
        if(!user.canManageEvents()){
            throw new UnauthorizedException("Insufficient permissions to create events");
        }
        
        Event event = eventValidationService.getValidEventById(eventId);
        if(!user.isAdmin() && !event.isOwnedBy(user)){
            throw new UnauthorizedException("You don't own this event");
        }
        repository.deleteEvent(eventId);
    }
}
