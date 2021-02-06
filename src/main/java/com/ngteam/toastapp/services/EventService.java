package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.EventDto;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity createEvent(String authorization, EventDto eventDto);
    ResponseEntity updateEventById(EventDto eventDto, long id);
    ResponseEntity getAllEvents();
    ResponseEntity getEventById(long id);
    ResponseEntity deleteEventById(long id);
}
