package com.bersyte.eventz.features.registrations;


import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/event")
public class AdminEventParticipationController {
    private final AdminEventParticipationService adminService;

    public AdminEventParticipationController(AdminEventParticipationService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/add-participant")
    public ResponseEntity<EventRegistrationResponse> registerUserToEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        EventRegistrationResponse response = adminService.registerUserToEvent(
                participantId, eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-participant")
    public ResponseEntity<EventRegistrationResponse> removeParticipantFromEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final EventRegistrationResponse response = adminService.removeParticipantFromEvent(
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
