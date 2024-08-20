package com.bersyte.eventz.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {

   private final EventService service;


   @PostMapping
   public ResponseEntity<EventResponseDto> create(
           @AuthenticationPrincipal UserDetails userDetails,
           @Valid @RequestBody EventRequestDto data
   ) {
       EventResponseDto responseDTO = this.service.createEvent(data, userDetails);
       return ResponseEntity.ok(responseDTO);
   }

   @GetMapping
   public ResponseEntity<List<EventResponseDto>> getAllEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
       List<EventResponseDto> allEvents = this.service.getAllEvents(page, size);
    return ResponseEntity.ok(allEvents);
   }

   @GetMapping("{id}")
   public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
       EventResponseDto response = this.service.getEventById(id);
       return ResponseEntity.ok(response);
   }

   @PutMapping("{id}")
   public ResponseEntity<EventResponseDto> updateEvent(
           @AuthenticationPrincipal UserDetails userDetails,
           @PathVariable Long id,
           @Valid @RequestBody EventRequestDto data
   ) {
       EventResponseDto responseDTO = this.service.updateEvent(id, data, userDetails);
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

   @GetMapping("/filter")
   public ResponseEntity<List<EventResponseDto>> filterEvents(
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size,
     @RequestParam(required = false) String title,
     @RequestParam(required = false) String location
   ) {
       List<EventResponseDto> allEvents = this.service.filterEvents(page, size, title, location);
     return ResponseEntity.ok(allEvents);
   }

    @GetMapping("/date")
    public ResponseEntity<List<EventResponseDto>> getEventsByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long eventDate
    ) {
        List<EventResponseDto> allEvents = this.service.getEventsByDate(page, size, eventDate);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDto>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<EventResponseDto> upcomingEvents = this.service.getUpcomingEvents(page, size);
        return ResponseEntity.ok(upcomingEvents);
    }
}
