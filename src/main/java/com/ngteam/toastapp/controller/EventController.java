package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.dto.in.EventDto;
import com.ngteam.toastapp.services.impl.EventServiceImpl;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/event")
@AllArgsConstructor
public class EventController extends ResponseCreator {

    private final EventServiceImpl eventService;

    @PostMapping
    ResponseEntity createEvent(@RequestHeader String authorization, @RequestBody EventDto eventDto) {
        return eventService.createEvent(authorization, eventDto);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventById(@RequestHeader String authorization, @RequestBody EventDto eventDto, @PathVariable long id) {
        return eventService.updateEventById(authorization, eventDto, id);
    }

    @GetMapping
    ResponseEntity getAllEvents(@RequestHeader String authorization) {
        return eventService.getAllEvents(authorization);
   }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventById(@RequestHeader String authorization, @PathVariable long id) {
        return eventService.getEventById(authorization, id);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventById(@RequestHeader String authorization, @PathVariable long id) {
        return eventService.deleteEventById(authorization, id);
    }
}