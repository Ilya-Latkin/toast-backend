package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.EventDto;
import com.ngteam.toastapp.dto.mapper.EventMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Event;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.CategoryRepository;
import com.ngteam.toastapp.repositories.EventRepository;
import com.ngteam.toastapp.repositories.EventTypeRepository;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/event")
@AllArgsConstructor
public class EventController extends ResponseCreator {

    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final CategoryRepository categoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity createEvent(@RequestHeader String authorization, @RequestBody EventDto eventDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
            Event event = new Event(eventDto.getName(), eventDto.getDescription(), eventDto.getDate(),
                    eventTypeRepository.findById(eventDto.getEventTypeId()).orElseThrow(NotFoundException::new),
                    categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(NotFoundException::new));

            event.setUser(userRepository.findByEmail(jwtHelper.getEmailFromToken(token))
                    //autizm
                    .orElseThrow(() -> new NotFoundException("User with email " + jwtHelper.getEmailFromToken(token) + " not found")));
            eventRepository.save(event);
            return createGoodResponse(eventMapper.toEventOutDtoConvert(event));
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventById(@RequestBody EventDto eventDto, @PathVariable long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        event.setName(eventDto.getName());
        event.setCategory(categoryRepository.findById(eventDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with id " + eventDto.getCategoryId() + " not found")));
        event.setDate(eventDto.getDate());
        event.setEventType(eventTypeRepository.findById(eventDto.getEventTypeId())
                .orElseThrow(() -> new NotFoundException("Event type with id " + eventDto.getEventTypeId() + " not found")));
        eventRepository.save(event);
        return createGoodResponse("Updated");
    }

    @GetMapping
    ResponseEntity getAllEvents() {
            List<Event> events = eventRepository.findAll();
            return createGoodResponse(eventMapper.toEventDtoList(events));
   }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventById(@PathVariable long id) {
            return createGoodResponse(eventMapper.toEventOutDtoConvert(eventRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"))));
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventById(@PathVariable long id) {
                eventRepository.delete(eventRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found")));
                return createGoodResponse("Deleted");
    }
}