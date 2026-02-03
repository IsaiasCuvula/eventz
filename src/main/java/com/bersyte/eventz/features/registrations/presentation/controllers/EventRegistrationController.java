package com.bersyte.eventz.features.registrations.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.registrations.application.dtos.*;
import com.bersyte.eventz.features.registrations.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/registrations")
public class EventRegistrationController {
    private final JoinEventUseCase joinEventUseCase;
    private final CancelEventRegistrationUseCase cancelRegistrationUseCase;
    private final GetUserValidTicketUseCase getUserValidTicketUseCase;
    private final UpdateTicketCheckInTokenUseCase updateTicketCheckInTokenUseCase;
    
    private final FetchEventParticipantsUseCase fetchEventParticipantsUseCase;
    private final CheckInUseCase checkInUseCase;
    
    public EventRegistrationController(JoinEventUseCase joinEventUseCase, CancelEventRegistrationUseCase cancelRegistrationUseCase, FetchEventParticipantsUseCase fetchEventParticipantsUseCase, GetUserValidTicketUseCase getUserValidTicketUseCase, UpdateTicketCheckInTokenUseCase updateTicketCheckInTokenUseCase, CheckInUseCase checkInUseCase) {
        this.joinEventUseCase = joinEventUseCase;
        this.cancelRegistrationUseCase = cancelRegistrationUseCase;
        this.fetchEventParticipantsUseCase = fetchEventParticipantsUseCase;
        this.getUserValidTicketUseCase = getUserValidTicketUseCase;
        this.updateTicketCheckInTokenUseCase = updateTicketCheckInTokenUseCase;
        this.checkInUseCase = checkInUseCase;
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    @PostMapping("/check-in/{checkInToken}/{deviceScannerId}")
    public ResponseEntity<TicketResponse> checkIn(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String checkInToken,
            @PathVariable String deviceScannerId
    ){
        CheckInRequest request = new CheckInRequest(
                userDetails.getUsername(),checkInToken, deviceScannerId
        );
        TicketResponse response =  checkInUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    @GetMapping("{eventId}")
    public ResponseEntity<PagedResult<EventParticipantResponse>> fetchEventParticipants(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable  String eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pagination pagination = new Pagination(page, size);
        FetchEventParticipantsRequest request = new FetchEventParticipantsRequest(
                eventId, userDetails.getUsername(),pagination
        );
        PagedResult<EventParticipantResponse> response = fetchEventParticipantsUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{eventId}/{userId}")
    public ResponseEntity<TicketResponse> joinEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String eventId,
            @PathVariable String userId
    ) {
        EventRegistrationRequest request = new EventRegistrationRequest(
                eventId, userId, userDetails.getUsername()
        );
        TicketResponse response = joinEventUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cancel/{ticketId}")
    public ResponseEntity<TicketResponse> cancelRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String ticketId
    ) {
        CancelEventRegistrationRequest request = new CancelEventRegistrationRequest(
                ticketId, userDetails.getUsername()
        );
        final TicketResponse response = cancelRegistrationUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("{eventId}/{userId}")
    public ResponseEntity<TicketResponse> getUserTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String eventId,
            @PathVariable String userId
    ){
        GetTicketRequest request = new GetTicketRequest(
                userDetails.getUsername(), userId, eventId
        );
        TicketResponse response = getUserValidTicketUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("{token}/rotate")
    public ResponseEntity<TicketResponse> updateCheckInToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String token
    ){
        UpdateTicketCheckInTokenRequest request = new UpdateTicketCheckInTokenRequest(
                userDetails.getUsername(), token
        );
        TicketResponse response = updateTicketCheckInTokenUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
