package com.event.management;

import com.event.management.model.ApiResponse;
import com.event.management.model.Event;
import com.event.management.model.EventRegistrationRequest;
import com.event.management.model.User;
import com.event.management.repository.EventRepository;
import com.event.management.repository.UserRepository;
import com.event.management.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventId(1);
        event.setName("Evento Test");
        event.setDate("12/12/2024");
        event.setLocation("Medellín");

        user = new User();
        user.setUserId(1);
        user.setName("Usuario Test");
    }

    @Test
    void testCreateEvent_Success() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        ResponseEntity<ApiResponse<Event>> response = eventService.createEvent(event);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getData());
        assertEquals("Evento Test", response.getBody().getData().getName());
    }

    @Test
    void testRegisterUserToEvent_Success() {
        EventRegistrationRequest request = new EventRegistrationRequest(user.getUserId());

        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.of(event));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = eventService.registerUserToEvent(event.getEventId(), request);

        assertEquals(200, response.getStatusCodeValue());
        verify(eventRepository, times(1)).save(event);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUserToEvent_EventNotFound() {
        EventRegistrationRequest request = new EventRegistrationRequest(user.getUserId());

        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventService.registerUserToEvent(event.getEventId(), request);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Evento no encontrado"));
    }

    @Test
    void testRegisterUserToEvent_UserNotFound() {
        EventRegistrationRequest request = new EventRegistrationRequest(user.getUserId());

        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.of(event));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventService.registerUserToEvent(event.getEventId(), request);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Usuario no encontrado"));
    }

    @Test
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findAll()).thenReturn(events);

        ResponseEntity<List<Event>> response = eventService.getAllEvents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetEventById_Found() {
        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.of(event));

        ResponseEntity<?> response = eventService.getEventById(event.getEventId());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Evento Test"));
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventService.getEventById(event.getEventId());

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Evento no encontrado"));
    }

    @Test
    void testUpdateEvent_Success() {
        Event updatedEvent = new Event();
        updatedEvent.setName("Evento Actualizado");
        updatedEvent.setDate("01/01/2025");
        updatedEvent.setLocation("Bogotá");

        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        ResponseEntity<?> response = eventService.updateEvent(event.getEventId(), updatedEvent);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Evento actualizado exitosamente"));
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(event.getEventId())).thenReturn(Optional.empty());

        Event updatedEvent = new Event();
        updatedEvent.setName("Evento Actualizado");

        ResponseEntity<?> response = eventService.updateEvent(event.getEventId(), updatedEvent);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Evento no encontrado"));
    }
}
