package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import com.ngteam.toastapp.dto.mapper.EventTypeMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Event;
import com.ngteam.toastapp.model.EventType;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.EventRepository;
import com.ngteam.toastapp.repositories.EventTypeRepository;
import com.ngteam.toastapp.services.EventTypeService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EventTypeServiceImpl extends ResponseCreator implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final EventTypeMapper eventTypeMapper;

    @Override
    public ResponseEntity createEventType(String authorization, EventTypeDto eventTypeDto) {
        User user = jwtHelper.getUserFromHeader(authorization);
        String eventTypeNameDto = eventTypeDto.getName();
        String convertedCategoryName = eventTypeNameDto.substring(0, 1).toUpperCase() + eventTypeNameDto.substring(1).toLowerCase();
        Optional<EventType> optionalUserEventType = eventTypeRepository.findByNameAndUserId(convertedCategoryName, user);
        if (optionalUserEventType.isPresent()) {
            return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED);
        }
//        EventType eventType = new EventType(convertedCategoryName, user);
        EventType eventType = EventType.builder()
                .name(convertedCategoryName)
                .user(user)
                .build();
        eventTypeRepository.save(eventType);
        return createGoodResponse(eventTypeMapper.toEventTypeDtoConvert(eventType));
    }

    @Override
    public ResponseEntity getAllEventTypes(String authorization) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        List<EventType> eventTypes = eventTypeRepository.findAllByUserId(userId);
        return createGoodResponse(eventTypeMapper.toEventTypeDtoList(eventTypes));
    }

    @Override
    public ResponseEntity getEventTypeById(String authorization, long id) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        return createGoodResponse(eventTypeMapper.toEventTypeOutDtoConvert(eventTypeRepository.findByIdAndUserId(id, userId).
                orElseThrow(() -> new NotFoundException("Event Type with id " + id + " not found"))));    }

    @Override
    public ResponseEntity updateEventTypeById(String authorization, long id, EventTypeDto eventTypeDto) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        EventType eventType = eventTypeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Event Type with id " + id + " not found"));
        eventType.setName(eventTypeDto.getName());
        eventTypeRepository.save(eventType);
        return createGoodResponse(eventTypeMapper.toEventTypeDtoConvert(eventType));
    }

    @Override
    public ResponseEntity deleteEventTypeById(String authorization, long id) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        EventType eventType = eventTypeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        List<Event> events = eventRepository.findAllByEventType(eventType);
        for(Event event: events) {
            event.setEventType(eventTypeRepository.findById(0L).
                    orElseThrow(() -> new NotFoundException("Сперва необходимо создать User(Admin) c EventTypeId = 0")));
            eventRepository.save(event);
        }
        eventTypeRepository.delete(eventType);
        return createGoodResponse("Deleted");
    }
}
