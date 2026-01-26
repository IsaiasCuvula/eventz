package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.dtos.EventsByOrganizerInput;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class FetchEventsByOrganizer implements UseCase<EventsByOrganizerInput, PagedResult<EventResponse>> {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserValidationService userValidationService;
    
    public FetchEventsByOrganizer(EventRepository eventRepository, EventMapper eventMapper, UserValidationService userValidationService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userValidationService = userValidationService;
    }
    
    @Override
    public PagedResult<EventResponse> execute(EventsByOrganizerInput input) {
        Pagination pagination = input.pagination();
        String organizerId = input.organizerId();
        AppUser organizer = userValidationService.getValidUserById(organizerId);
        PagedResult<Event> pagedResult = eventRepository.fetchEventsByOrganizer(organizer.getId(), pagination);
        return eventMapper.toPagedResponse(pagedResult);
    }
}
