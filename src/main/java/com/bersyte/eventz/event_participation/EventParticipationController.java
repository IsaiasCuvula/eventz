package com.bersyte.eventz.event_participation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/event-registration")
public class EventParticipationController {

    private final EventParticipationService registrationService;

    @PostMapping("{eventId}")
    public ResponseEntity<EventParticipationResponseDTO> registerToEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long eventId
    ) {
        EventParticipationResponseDTO response = registrationService.registerUserToEvent(
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
    public ResponseEntity<EventParticipationResponseDTO> removeParticipantFromEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final EventParticipationResponseDTO response = registrationService.removeParticipantFromEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-participant")
    public ResponseEntity<EventParticipationResponseDTO> organizerAddUserToHisEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId

    ) {
        final EventParticipationResponseDTO response = registrationService.organizerAddUserToHisEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
