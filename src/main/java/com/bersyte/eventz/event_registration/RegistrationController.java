package com.bersyte.eventz.event_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/event-registration")
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping("{id}")
    public ResponseEntity<RegistrationResponseDTO> registerToEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id
    ) {
        RegistrationResponseDTO response = registrationService.registerToEvent(
                id, userDetails
        );
        return ResponseEntity.ok(response);
    }


}
