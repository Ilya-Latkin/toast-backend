package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import com.ngteam.toastapp.dto.mapper.EventTypeMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.EventType;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.EventTypeRepository;
import com.ngteam.toastapp.repositories.UserRepository;
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
    private final JwtHelper jwtHelper;
    private final EventTypeMapper eventTypeMapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity createEventType(String authorization, EventTypeDto eventTypeDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        String emailFromToken = jwtHelper.getEmailFromToken(token);
        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new NotFoundException("autizm")); // <--- autizm / shobi ne rugalsa
        String eventTypeNameDto = eventTypeDto.getName();
        String convertedCategoryName = eventTypeNameDto.substring(0, 1).toUpperCase() + eventTypeNameDto.substring(1).toLowerCase();
        Optional<EventType> optionalUserEventType = eventTypeRepository.findByNameAndUserId(convertedCategoryName, user);
        if (optionalUserEventType.isPresent()) {
            return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED);
        }
        EventType eventType = new EventType(convertedCategoryName, user);
        eventTypeRepository.save(eventType);
        return createGoodResponse(eventTypeMapper.toEventTypeDtoConvert(eventType));
    }

    @Override
    public ResponseEntity getAllEventTypes() {
        List<EventType> eventTypes = eventTypeRepository.findAll();
        return createGoodResponse(eventTypeMapper.toEventTypeDtoList(eventTypes));
    }

    @Override
    public ResponseEntity getEventTypeById(long id) {
        return createGoodResponse(eventTypeMapper.toEventTypeOutDtoConvert(eventTypeRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Event Type with id " + id + " not found"))));    }

    @Override
    public ResponseEntity updateEventTypeById(long id, EventTypeDto eventTypeDto) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event Type with id " + id + " not found"));
        eventType.setName(eventTypeDto.getName());
        eventTypeRepository.save(eventType);
        return createGoodResponse(eventTypeMapper.toEventTypeDtoConvert(eventType));    }

    @Override
    public ResponseEntity deleteEventTypeById(long id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        eventTypeRepository.delete(eventType);
        return createGoodResponse("Deleted");
    }
}
