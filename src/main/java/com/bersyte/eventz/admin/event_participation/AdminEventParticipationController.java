package com.bersyte.eventz.admin.event_participation;


import com.bersyte.eventz.event_participation.EventParticipationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/event")
public class AdminEventParticipationController {
    private final AdminEventParticipationService adminService;

    @PostMapping("/add-participant")
    public ResponseEntity<EventParticipationResponseDTO> registerUserToEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        EventParticipationResponseDTO response = adminService.registerUserToEvent(
                participantId, eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-participant")
    public ResponseEntity<EventParticipationResponseDTO> removeParticipantFromEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final EventParticipationResponseDTO response = adminService.removeParticipantFromEvent(
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
