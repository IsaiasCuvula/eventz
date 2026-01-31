package com.bersyte.eventz.features.registrations.presentation.controllers;

import com.bersyte.eventz.features.registrations.EventParticipationService;
import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/event-registration")
public class EventParticipationController {

    private final EventParticipationService registrationService;

    public EventParticipationController(EventParticipationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("{eventId}")
    public ResponseEntity<EventRegistrationResponse> registerToEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long eventId
    ) {
        EventRegistrationResponse response = registrationService.registerUserToEvent(
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
    public ResponseEntity<EventRegistrationResponse> removeParticipantFromEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final EventRegistrationResponse response = registrationService.removeParticipantFromEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-participant")
    public ResponseEntity<EventRegistrationResponse> organizerAddUserToHisEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId

    ) {
        final EventRegistrationResponse response = registrationService.organizerAddUserToHisEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
