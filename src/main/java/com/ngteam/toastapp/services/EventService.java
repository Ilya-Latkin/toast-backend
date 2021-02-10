package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.EventDto;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity createEvent(String authorization, EventDto eventDto);
    ResponseEntity updateEventById(String authorization, EventDto eventDto, long id);
    ResponseEntity getAllEvents(String authorization);
    ResponseEntity getEventById(String authorization, long id);
    ResponseEntity deleteEventById(String authorization, long id);
}