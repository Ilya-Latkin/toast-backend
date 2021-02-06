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
import com.ngteam.toastapp.services.EventService;
import com.ngteam.toastapp.services.impl.EventServiceImpl;
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

    private final EventServiceImpl eventService;
    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final CategoryRepository categoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity createEvent(@RequestHeader String authorization, @RequestBody EventDto eventDto) {
        return eventService.createEvent(authorization, eventDto);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventById(@RequestBody EventDto eventDto, @PathVariable long id) {
        return eventService.updateEventById(eventDto, id);
    }

    @GetMapping
    ResponseEntity getAllEvents() {
        return eventService.getAllEvents();
   }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventById(@PathVariable long id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventById(@PathVariable long id) {
        return eventService.deleteEventById(id);
    }
}