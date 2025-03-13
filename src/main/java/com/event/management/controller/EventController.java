package com.event.management.controller;

import com.event.management.model.ApiResponse;
import com.event.management.model.Event;
import com.event.management.model.EventRegistrationRequest;
import com.event.management.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Event>> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerUserToEvent(@PathVariable("id") Integer eventId, @RequestBody EventRegistrationRequest request) {
        return eventService.registerUserToEvent(eventId, request);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Integer eventId) {
        return eventService.getEventById(eventId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") Integer eventId,
                                         @RequestBody Event updatedEvent) {
        return eventService.updateEvent(eventId, updatedEvent);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEventsByUserId(@PathVariable("userId") Integer userId) {
        return eventService.getEventsByUserId(userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer eventId) {
        return eventService.deleteEvent(eventId);
    }
}
