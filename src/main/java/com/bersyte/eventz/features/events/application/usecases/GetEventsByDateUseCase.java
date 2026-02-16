package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventByDateInput;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

import java.time.LocalDateTime;


public class GetEventsByDateUseCase implements UseCase<EventByDateInput, PagedResult<EventResponse>> {
    private final EventRepository repository;
    private final EventMapper mapper;
    
    public GetEventsByDateUseCase(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public PagedResult<EventResponse> execute(EventByDateInput input) {
        Pagination pagination = input.pagination();
        LocalDateTime start = input.startDate();
        LocalDateTime end = input.endDate();
        
        if(start == null || end == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        
        PagedResult<Event> pagedResult = repository.fetchEventsByDate(pagination, input.startDate(), input.endDate());
        return mapper.toPagedResponse(pagedResult);
    }
}
