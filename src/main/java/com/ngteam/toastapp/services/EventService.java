package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.EventDto;
import com.ngteam.toastapp.model.Category;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface EventService {
    ResponseEntity createEvent(String authorization, EventDto eventDto) throws ParseException;
    ResponseEntity updateEventById(String authorization, EventDto eventDto, long id);
    ResponseEntity getAllEvents(String authorization);
    ResponseEntity getEventById(String authorization, long id);
    ResponseEntity deleteEventById(String authorization, long id);
}