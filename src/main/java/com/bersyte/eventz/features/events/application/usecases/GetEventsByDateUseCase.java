package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.EventByDateParams;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

import java.util.Date;
import java.util.List;

public class GetEventsByDateUseCase implements UseCase<EventByDateParams, PagedResult<EventResponse>> {
    private final EventRepository repository;
    private final EventMapper mapper;
    
    public GetEventsByDateUseCase(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public PagedResult<EventResponse> execute(EventByDateParams params) {
         Pagination pagination = params.pagination();
         Date date = params.date();
         PagedResult<Event> result = repository.fetchEventsByDate(pagination, date);
         List<EventResponse> events = result.data().stream().map(mapper::toResponse).toList();
         return new PagedResult<>(
                 events,
                 result.totalElements(), result.totalPages(), result.isLast()
         );
    }
}
