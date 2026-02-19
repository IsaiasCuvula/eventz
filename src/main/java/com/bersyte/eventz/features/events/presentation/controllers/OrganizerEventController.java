package com.bersyte.eventz.features.events.presentation.controllers;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.auth.infrastructure.persistence.AppUserPrincipal;
import com.bersyte.eventz.features.events.application.dtos.*;
import com.bersyte.eventz.features.events.application.usecases.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @Valid @RequestBody CreateEventRequest request
    ) {
        CreateEventInput input = new CreateEventInput(
                currentUser.id(),request
        );
        EventResponse response = createEventUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
    @PutMapping("{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @Valid @RequestBody UpdateEventRequest request,
            @PathVariable UUID eventId
    ) {
        UpdateEventInput input = new UpdateEventInput(
                request , eventId,  currentUser.id()
        );
        EventResponse response = updateEventUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{eventId}")
    public ResponseEntity<String> deleteEvent(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable UUID eventId
    ) {
        DeleteEventRequest input = new DeleteEventRequest(
                eventId,  currentUser.id()
        );
        deleteEventUseCase.execute(input);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/organizer")
    public ResponseEntity<PagedResult<EventResponse>> eventByOrganizer(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pagination pagination = new Pagination(page, size);
        EventsByOrganizerRequest request = new EventsByOrganizerRequest(
                pagination,  currentUser.id()
        );
        PagedResult<EventResponse>  response = fetchEventsByOrganizerUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
