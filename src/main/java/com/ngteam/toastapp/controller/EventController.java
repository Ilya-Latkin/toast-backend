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

    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final CategoryRepository categoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity createEvent(@RequestHeader String authorization, @RequestBody EventDto eventDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Event event = new Event(eventDto.getName(), eventDto.getDescription(), eventDto.getDate(),
                    eventTypeRepository.findById(eventDto.getEventTypeId()).orElseThrow(NotFoundException::new),
                    categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(NotFoundException::new));

            String emailFromToken = jwtHelper.getEmailFromToken(token);
            Optional<User> optionalUser = userRepository.findByEmail(emailFromToken);
            User user = optionalUser.get();
            event.setUser(user);
            eventRepository.save(event);
            return createGoodResponse(eventDto);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateEventById(@RequestHeader String authorization, @RequestBody EventDto eventDto, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<Event> eventOptional = eventRepository.findById(id);
            if(eventOptional.isPresent()) {
                Event event = eventOptional.get();
                event.setName(eventDto.getName());
                event.setCategory(categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(NotFoundException::new));
                event.setDate(eventDto.getDate());
                event.setEventType(eventTypeRepository.findById(eventDto.getEventTypeId()).orElseThrow(NotFoundException::new));
                eventRepository.save(event);
                return createGoodResponse();
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping
    ResponseEntity getAllEvents(@RequestHeader String authorization){
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            List<Event> events = eventRepository.findAll();
            return createGoodResponse(eventMapper.toEventDtoList(events));
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getEventById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
            return createGoodResponse(eventMapper.toEventOutDtoConvert(event));
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteEventById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<Event> eventOptional = eventRepository.findById(id);
            if(eventOptional.isPresent()) {
                Event event = eventOptional.get();
                eventRepository.delete(event);
                return createGoodResponse("Deleted");
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }
}