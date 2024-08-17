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

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        final String response = registrationService.cancelRegistration(userDetails, id);
        return ResponseEntity.ok(response);
    }


}
