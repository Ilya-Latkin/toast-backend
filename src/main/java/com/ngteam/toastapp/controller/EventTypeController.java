package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.dto.in.EventTypeDto;
import com.ngteam.toastapp.services.impl.EventTypeServiceImpl;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/type")
@AllArgsConstructor
public class EventTypeController extends ResponseCreator {

    private final EventTypeServiceImpl eventTypeService;

    @PostMapping
    ResponseEntity createEventType(@RequestHeader String authorization, @RequestBody EventTypeDto eventTypeDto) {
        return eventTypeService.createEventType(authorization, eventTypeDto);
    }

    @GetMapping
    ResponseEntity getAllEventTypes() {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventTypeById(@PathVariable long id) {
        return eventTypeService.getEventTypeById(id);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventTypeById(@PathVariable long id, @RequestBody EventTypeDto eventTypeDto) {
        return eventTypeService.updateEventTypeById(id, eventTypeDto);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventTypeById(@PathVariable long id) {
        return eventTypeService.deleteEventTypeById(id);
    }
}