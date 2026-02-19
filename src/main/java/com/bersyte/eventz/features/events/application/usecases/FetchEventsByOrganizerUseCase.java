package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.dtos.EventsByOrganizerRequest;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.util.UUID;

public class FetchEventsByOrganizerUseCase implements UseCase<EventsByOrganizerRequest, PagedResult<EventResponse>> {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserValidationService userValidationService;
    
    public FetchEventsByOrganizerUseCase(EventRepository eventRepository, EventMapper eventMapper, UserValidationService userValidationService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userValidationService = userValidationService;
    }
    
    @Override
    public PagedResult<EventResponse> execute(EventsByOrganizerRequest input) {
        Pagination pagination = input.pagination();
        UUID organizerId = input.organizerId();
        AppUser organizer = userValidationService.getValidUserById(organizerId);
        PagedResult<Event> pagedResult = eventRepository.fetchEventsByOrganizer(organizer.getId(), pagination);
        return eventMapper.toPagedResponse(pagedResult);
    }
}
