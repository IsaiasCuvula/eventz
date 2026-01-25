package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class FetchEventsUseCase implements UseCase<Pagination, PagedResult<EventResponse>> {
    private final EventRepository repository;
    private final EventMapper mapper;
    
    public FetchEventsUseCase(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override
    public PagedResult<EventResponse> execute(Pagination pagination) {
        PagedResult<Event> pagedResult = repository.fetchEvents(pagination);
        return mapper.toPagedResponse(pagedResult);
    }
}
