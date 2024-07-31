package com.beryste.eventz.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.beryste.eventz.dto.EventRequestDTO;
import com.beryste.eventz.dto.EventResponseDTO;
import com.beryste.eventz.models.Event;
import com.beryste.eventz.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;




@RestController
@RequestMapping("/v1/api/eventz")
public class EventController {

   private final EventService service;

   public EventController(EventService service){
    this.service = service;
   }

   @PostMapping
   public ResponseEntity<Event> create(@RequestBody EventRequestDTO data) {
       Event event = this.service.createEvent(data);
       return ResponseEntity.ok(event);
   }

   // TODO- pagination
   @GetMapping
   public ResponseEntity<List<EventResponseDTO>> getAllEvents(
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size
    ) {
    List<EventResponseDTO> allEvents = this.service.getAllEvents();
    return ResponseEntity.ok(allEvents);
   }
}
