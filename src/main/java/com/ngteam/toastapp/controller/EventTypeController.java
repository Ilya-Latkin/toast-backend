package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import com.ngteam.toastapp.dto.mapper.EventTypeMapper;
import com.ngteam.toastapp.model.EventType;
import com.ngteam.toastapp.model.User;
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
@RequestMapping(path = "/type")
@AllArgsConstructor
public class EventTypeController extends ResponseCreator {

    private final EventTypeRepository eventTypeRepository;
    private final JwtHelper jwtHelper;
    private final EventTypeMapper eventTypeMapper;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity createEventType(@RequestHeader String authorization, @RequestBody EventTypeDto eventTypeDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<EventType> eventTypeOptional = eventTypeRepository.findByName(eventTypeDto.getName());
            if(!eventTypeOptional.isPresent()) {
                String string = eventTypeDto.getName();
                String convertedString = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
                EventType eventType = new EventType(convertedString);
                String emailFromToken = jwtHelper.getEmailFromToken(token);
                Optional<User> optionalUser = userRepository.findByEmail(emailFromToken);
                User user = optionalUser.get();
                eventType.setUser(user);
                eventTypeRepository.save(eventType);
                return createGoodResponse(eventType);
            } else return createErrorResponse(ErrorEntity.EVENT_TYPE_ALREADY_CREATED);
        }
        else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping
    ResponseEntity getAllEventType(@RequestHeader String authorization) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            List<EventType> eventTypes = eventTypeRepository.findAll();
            return createGoodResponse(eventTypeMapper.toEventTypeDtoList(eventTypes));
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventTypeById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<EventType> eventTypeOptional = eventTypeRepository.findById(id);
            if(eventTypeOptional.isPresent()) {
                EventType eventType = eventTypeOptional.get();
                return createGoodResponse(eventTypeMapper.toEventTypeOutDtoConvert(eventType));
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventTypeById(@RequestHeader String authorization, @PathVariable long id, @RequestBody EventTypeDto eventTypeDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<EventType> eventTypeOptional = eventTypeRepository.findById(id);
            if(eventTypeOptional.isPresent()) {
                EventType eventType = eventTypeOptional.get();
                eventType.setName(eventTypeDto.getName());
                eventTypeRepository.save(eventType);
                return createGoodResponse(eventType);
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventTypeById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<EventType> eventTypeOptional = eventTypeRepository.findById(id);
            if(eventTypeOptional.isPresent()) {
                EventType eventType = eventTypeOptional.get();
                eventTypeRepository.delete(eventType);
                return createGoodResponse("Deleted");
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }
}