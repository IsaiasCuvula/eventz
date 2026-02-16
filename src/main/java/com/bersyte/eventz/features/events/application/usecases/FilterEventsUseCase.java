package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.dtos.EventSearchFilter;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class FilterEventsUseCase implements UseCase<EventSearchFilter, PagedResult<EventResponse>> {
    private final EventRepository repository;
    private final EventMapper mapper;
    
    public FilterEventsUseCase(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public PagedResult<EventResponse> execute(EventSearchFilter filter) {
        PagedResult<Event> pagedResult = repository.filterEvents(
                filter.title(), filter.location(), filter.pagination());
        return mapper.toPagedResponse(pagedResult);
    }
}
