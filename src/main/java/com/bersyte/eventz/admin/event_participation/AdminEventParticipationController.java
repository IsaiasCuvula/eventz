package com.bersyte.eventz.admin.event_participation;


import com.bersyte.eventz.event_participation.EventParticipationResponseDto;
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
    public ResponseEntity<EventParticipationResponseDto> registerUserToEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        EventParticipationResponseDto response = adminService.registerUserToEvent(
                participantId, eventId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-participant")
    public ResponseEntity<EventParticipationResponseDto> removeParticipantFromEvent(
            @RequestParam Long participantId,
            @RequestParam Long eventId
    ) {
        final EventParticipationResponseDto response = adminService.removeParticipantFromEvent(
                participantId,
                eventId
        );
        return ResponseEntity.ok(response);
    }
}
