package com.bersyte.eventz.features.registrations.presentation.controllers;

import com.bersyte.eventz.features.registrations.EventParticipationService;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/registrations")
public class EventParticipationController {

    private final EventParticipationService registrationService;

    public EventParticipationController(EventParticipationService registrationService) {
        this.registrationService = registrationService;
    }
    
 
    
    @PostMapping("/check-in/{checkInToken}")
    public ResponseEntity<?> checkIn(@PathVariable String checkInToken){
    
    }

    @PostMapping("{eventId}")
    public ResponseEntity<TicketResponse> registerToEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String eventId
    ) {
        TicketResponse response = registrationService.registerUserToEvent(
                eventId, userDetails
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        final String response = registrationService.cancelRegistration(userDetails, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-participant")
    public ResponseEntity<TicketResponse> removeParticipantFromEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final TicketResponse response = registrationService.removeParticipantFromEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-participant")
    public ResponseEntity<TicketResponse> organizerAddUserToHisEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId

    ) {
        final TicketResponse response = registrationService.organizerAddUserToHisEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
