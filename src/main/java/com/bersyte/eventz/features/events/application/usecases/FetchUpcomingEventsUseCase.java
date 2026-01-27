package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

import java.time.Clock;
import java.time.LocalDateTime;


public class FetchUpcomingEventsUseCase implements UseCase<Pagination, PagedResult<EventResponse>> {
    private final EventRepository repository;
    private final EventMapper mapper;
    private final Clock clock;
    
    public FetchUpcomingEventsUseCase(EventRepository repository, EventMapper mapper, Clock clock) {
        this.repository = repository;
        this.mapper = mapper;
        this.clock = clock;
    }
    
    @Override
    public PagedResult<EventResponse> execute(Pagination pagination) {
        LocalDateTime now = LocalDateTime.now(clock);
        PagedResult<Event> result = repository.fetchUpcomingEvents(now, pagination);
        return mapper.toPagedResponse(result);
    }
}
