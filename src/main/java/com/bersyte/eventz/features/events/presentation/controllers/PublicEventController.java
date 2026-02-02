package com.bersyte.eventz.features.events.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.*;
import com.bersyte.eventz.features.events.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/events")
public class PublicEventController {
    
    private final GetEventsByDateUseCase getEventsByDateUseCase;
    private final GetEventByIdUseCase getEventByIdUseCase;
    private final FetchEventsUseCase fetchEventsUseCase;
    private final FetchUpcomingEventsUseCase fetchUpcomingEventsUseCase;
    private final FilterEventsUseCase filterEventsUseCase;
    

    
    public PublicEventController(
                           GetEventsByDateUseCase getEventsByDateUseCase,
                           GetEventByIdUseCase getEventByIdUseCase,
                           FetchEventsUseCase fetchEventsUseCase,
                           FetchUpcomingEventsUseCase fetchUpcomingEventsUseCase,
                           FilterEventsUseCase filterEventsUseCase
    ){
        this.getEventsByDateUseCase = getEventsByDateUseCase;
        this.getEventByIdUseCase = getEventByIdUseCase;
        this.fetchEventsUseCase = fetchEventsUseCase;
        this.fetchUpcomingEventsUseCase = fetchUpcomingEventsUseCase;
        this.filterEventsUseCase = filterEventsUseCase;
    }

   
   @GetMapping
   public ResponseEntity<PagedResult<EventResponse>> fetchEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pagination pagination = new Pagination(page, size);
        PagedResult<EventResponse> events = fetchEventsUseCase.execute(pagination);
        return ResponseEntity.ok(events);
   }

   @GetMapping("{id}")
   public ResponseEntity<EventResponse> getEventById(@PathVariable String id) {
       EventByIdInput input = new EventByIdInput("user-email-private-event", id);
       EventResponse response = getEventByIdUseCase.execute(input);
       return ResponseEntity.status(HttpStatus.OK).body(response);
   }
   

   @GetMapping("/filter")
   public ResponseEntity< PagedResult<EventResponse>> filterEvents(
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size,
     @RequestParam(required = false) String title,
     @RequestParam(required = false) String location,
     @RequestParam LocalDateTime startDate,
     @RequestParam LocalDateTime endDate
   ) {
       Pagination pagination = new Pagination(page, size);
       EventSearchFilter filter = new EventSearchFilter(title, location,startDate, endDate ,pagination);
       PagedResult<EventResponse> events = filterEventsUseCase.execute (filter);
     return ResponseEntity.ok(events);
   }

    @GetMapping("/date")
    public ResponseEntity< PagedResult<EventResponse>> getEventsByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        Pagination pagination = new Pagination(page, size);
        EventByDateInput input = new EventByDateInput(pagination,startDate, endDate);
        PagedResult<EventResponse> events = getEventsByDateUseCase.execute(input);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    public ResponseEntity< PagedResult<EventResponse>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pagination pagination = new Pagination(page, size);
        PagedResult<EventResponse>  upcomingEvents =
                fetchUpcomingEventsUseCase.execute(pagination);
        return ResponseEntity.ok(upcomingEvents);
    }
}
