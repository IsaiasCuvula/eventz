package com.bersyte.eventz.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.models.Event;
import com.bersyte.eventz.services.EventService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;




@RestController
@RequestMapping("/v1/eventz")
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

   @GetMapping
   public ResponseEntity<List<EventResponseDTO>> getAllEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
    List<EventResponseDTO> allEvents = this.service.getAllEvents(page, size);
    return ResponseEntity.ok(allEvents);
   }
}