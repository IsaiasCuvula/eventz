package com.bersyte.eventz.event_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/event-registration")
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping("{id}")
    public ResponseEntity<RegistrationResponseDTO> registerToEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        RegistrationResponseDTO response = registrationService.registerUserToEvent(
                id, userDetails
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
    public ResponseEntity<RegistrationResponseDTO> removeParticipantFromEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final RegistrationResponseDTO response = registrationService.removeParticipantFromEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-participant")
    public ResponseEntity<RegistrationResponseDTO> organizerAddUserToHisEvent(
            @AuthenticationPrincipal UserDetails organizerDetails,
            @RequestParam Long participantId,
            @RequestParam Long eventId

    ) {
        final RegistrationResponseDTO response = registrationService.organizerAddUserToHisEvent(
                organizerDetails,
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
