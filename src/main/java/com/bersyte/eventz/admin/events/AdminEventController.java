package com.bersyte.eventz.admin.events;

import com.bersyte.eventz.events.EventRequestDto;
import com.bersyte.eventz.events.EventResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final AdminEventService service;

    @PutMapping("{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @Valid @RequestBody EventRequestDto data,
            @PathVariable Long id
    ) {
        EventResponseDto response = service.updateEvent(id, data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        this.service.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }
}
