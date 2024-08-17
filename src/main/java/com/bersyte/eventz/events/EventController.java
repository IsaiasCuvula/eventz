package com.bersyte.eventz.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/eventz")
public class EventController {

   private final EventService service;


   @PostMapping
   public ResponseEntity<EventResponseDTO> create(
           @AuthenticationPrincipal UserDetails userDetails,
           @Valid @RequestBody EventRequestDTO data
   ) {
       EventResponseDTO responseDTO = this.service.createEvent(data, userDetails);
       return ResponseEntity.ok(responseDTO);
   }

   @GetMapping
   public ResponseEntity<List<EventResponseDTO>> getAllEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
    List<EventResponseDTO> allEvents = this.service.getAllEvents(page, size);
    return ResponseEntity.ok(allEvents);
   }

   @GetMapping("{id}")
   public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        EventResponseDTO response = this.service.getEventById(id);
       return ResponseEntity.ok(response);
   }

   @PutMapping("{id}")
   public ResponseEntity<EventResponseDTO> updateEvent(
           @AuthenticationPrincipal UserDetails userDetails,
           @PathVariable Long id,
           @Valid @RequestBody EventRequestDTO data
   ) {
       EventResponseDTO responseDTO = this.service.updateEvent(id, data, userDetails);
       return ResponseEntity.ok(responseDTO);
   }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        this.service.deleteEvent(id, userDetails);
        return ResponseEntity.ok("Event deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<EventResponseDTO> updateEventAdmin(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO data
    ) {
        EventResponseDTO response = service.adminUpdateEvent(id, data);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteEventAdmin(@PathVariable Long id) {
        this.service.adminDeleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
   }

   @GetMapping("/filter")
   public ResponseEntity<List<EventResponseDTO>> filterEvents(
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size,
     @RequestParam(required = false) String title,
     @RequestParam(required = false) String location
   ) {
     List<EventResponseDTO> allEvents = this.service.filterEvents(page, size, title, location);
     return ResponseEntity.ok(allEvents);
   }

    @GetMapping("/date")
    public ResponseEntity<List<EventResponseDTO>> getEventsByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long eventDate
    ) {
        List<EventResponseDTO> allEvents = this.service.getEventsByDate(page, size, eventDate);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<EventResponseDTO> upcomingEvents = this.service.getUpcomingEvents(page, size);
        return ResponseEntity.ok(upcomingEvents);
    }
}
