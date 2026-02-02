package com.bersyte.eventz.features.events.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.application.dtos.*;
import com.bersyte.eventz.features.events.application.usecases.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizers/events")
@PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
public class OrganizerEventController {
    
    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final FetchEventsByOrganizerUseCase fetchEventsByOrganizerUseCase;
    
    
    public OrganizerEventController(CreateEventUseCase createEventUseCase, UpdateEventUseCase updateEventUseCase, DeleteEventUseCase deleteEventUseCase, FetchEventsByOrganizerUseCase fetchEventsByOrganizerUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.fetchEventsByOrganizerUseCase = fetchEventsByOrganizerUseCase;
    }
    
    @PostMapping
    public ResponseEntity<EventResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateEventRequest request
    ) {
        CreateEventInput input = new CreateEventInput(
                userDetails.getUsername(),request
        );
        EventResponse response = createEventUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
        return ResponseEntity.noContent().build();
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
