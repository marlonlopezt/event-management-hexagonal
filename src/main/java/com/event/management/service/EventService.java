package com.event.management.service;

import com.event.management.model.ApiResponse;
import com.event.management.model.Event;
import com.event.management.model.User;
import com.event.management.repository.EventRepository;
import com.event.management.repository.UserRepository;
import com.event.management.model.EventRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<Event>> createEvent(Event event) {
        try {
            Event savedEvent = eventRepository.save(event);
            ApiResponse<Event> response = new ApiResponse<>(savedEvent, "Evento creado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Event> response = new ApiResponse<>(null, "Error al crear el evento: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    public ResponseEntity<?> registerUserToEvent(Integer eventId, EventRegistrationRequest request) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<User> userOpt = userRepository.findById(request.getUserId());

        if (eventOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"mensaje\": \"Evento no encontrado\"}");
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"mensaje\": \"Usuario no encontrado\"}");
        }

        Event event = eventOpt.get();
        User user = userOpt.get();

        // Sincronización bidireccional
        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
            user.getRegisteredEvents().add(event); // Asegura la sincronización
            userRepository.save(user);            // Guarda el usuario con la relación actualizada
            eventRepository.save(event);          // Guarda el evento actualizado
        }

        return ResponseEntity.ok("{\"mensaje\": \"Usuario registrado en el evento exitosamente\"}");
    }

    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }
    public ResponseEntity<?> getEventById(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            return ResponseEntity.ok(eventOpt.get());
        } else {
            return ResponseEntity.status(404).body("{\"mensaje\": \"Evento no encontrado\"}");
        }
    }
    public ResponseEntity<?> updateEvent(Integer eventId, Event updatedEvent) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event existingEvent = eventOpt.get();
            existingEvent.setName(updatedEvent.getName());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setLocation(updatedEvent.getLocation());

            Event savedEvent = eventRepository.save(existingEvent);
            ApiResponse<Event> response = new ApiResponse<>(savedEvent, "Evento actualizado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("{\"mensaje\": \"Evento no encontrado\"}");
        }
    }

    public ResponseEntity<?> getEventsByUserId(Integer userId) {
        List<Event> events = eventRepository.findEventsByUserId(userId);
        if (events.isEmpty()) {
            return ResponseEntity.status(404).body("{\"mensaje\": \"No se encontraron eventos para el usuario\"}");
        }
        return ResponseEntity.ok(events);
    }

    public ResponseEntity<?> deleteEvent(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            eventRepository.deleteById(eventId);
            return ResponseEntity.ok("{\"mensaje\": \"Evento eliminado exitosamente\"}");
        } else {
            return ResponseEntity.status(404).body("{\"mensaje\": \"Evento no encontrado\"}");
        }
    }
}
