package com.bersyte.eventz.features.events.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.*;
import com.bersyte.eventz.features.events.application.usecases.*;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.users.application.dtos.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1/events")
public class EventController {

   private final CreateEventUseCase createEventUseCase;
   private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final FetchEventsByOrganizerUseCase fetchEventsByOrganizerUseCase;
    private final GetEventsByDateUseCase getEventsByDateUseCase;
    private final GetEventByIdUseCase getEventByIdUseCase;
    private final FetchEventsUseCase fetchEventsUseCase;
    private final FetchUpcomingEventsUseCase fetchUpcomingEventsUseCase;
    private final FilterEventsUseCase filterEventsUseCase;

    
    public EventController(CreateEventUseCase createEventUseCase, UpdateEventUseCase updateEventUseCase, DeleteEventUseCase deleteEventUseCase, FetchEventsByOrganizerUseCase fetchEventsByOrganizerUseCase, GetEventsByDateUseCase getEventsByDateUseCase, GetEventByIdUseCase getEventByIdUseCase, FetchEventsUseCase fetchEventsUseCase, FetchUpcomingEventsUseCase fetchUpcomingEventsUseCase, FilterEventsUseCase filterEventsUseCase){
       this.createEventUseCase = createEventUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.fetchEventsByOrganizerUseCase = fetchEventsByOrganizerUseCase;
        this.getEventsByDateUseCase = getEventsByDateUseCase;
        this.getEventByIdUseCase = getEventByIdUseCase;
        this.fetchEventsUseCase = fetchEventsUseCase;
        this.fetchUpcomingEventsUseCase = fetchUpcomingEventsUseCase;
        this.filterEventsUseCase = filterEventsUseCase;
    }

   @PostMapping
   public ResponseEntity<EventResponse> create(
           @AuthenticationPrincipal UserDetails userDetails,
           @Valid @RequestBody CreateEventRequest request
   ) {
       CreateEventInput input = new CreateEventInput(
               userDetails.getUsername(),
               request
       );
       EventResponse response = createEventUseCase.execute(input);
       return ResponseEntity.ok(response);
   }

   @GetMapping
   public ResponseEntity<PagedResult<EventResponse>> getEvents(
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
       return ResponseEntity.ok(response);
   }

   @PutMapping("{id}")
   public ResponseEntity<EventResponse> updateEvent(
           @AuthenticationPrincipal UserDetails userDetails,
           @PathVariable String id,
           @Valid @RequestBody UpdateEventRequest request
   ) {
        UpdateEventInput input = new UpdateEventInput(
                request , id,  userDetails.getUsername()
        );
       EventResponse response = updateEventUseCase.execute(input);
       return ResponseEntity.ok(response);
   }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ) {
        DeleteEventInput input = new DeleteEventInput(
                id,  userDetails.getUsername()
        );
        deleteEventUseCase.execute(input);
        return ResponseEntity.ok("EventEntity deleted successfully");
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
    
    
    @GetMapping("/organizer")
    public ResponseEntity<PagedResult<EventResponse>> eventByOrganizer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Pagination pagination = new Pagination(page, size);
        EventsByOrganizerInput input = new EventsByOrganizerInput(
                pagination,  userDetails.getUsername()
        );
        PagedResult<EventResponse>  response = fetchEventsByOrganizerUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
}
