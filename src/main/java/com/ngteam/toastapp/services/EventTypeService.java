package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.EventTypeDto;
import org.springframework.http.ResponseEntity;

public interface EventTypeService {
    ResponseEntity createEventType(String authorization, EventTypeDto eventTypeDto);
    ResponseEntity getAllEventTypes();
    ResponseEntity getEventTypeById(long id);
    ResponseEntity updateEventTypeById(long id, EventTypeDto eventTypeDto);
    ResponseEntity deleteEventTypeById(long id);
}
