package com.ngteam.toastapp.services.impl;

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
import com.ngteam.toastapp.services.EventService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EventServiceImpl extends ResponseCreator implements EventService {

    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final CategoryRepository categoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity createEvent(String authorization, EventDto eventDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        String emailFromToken = jwtHelper.getEmailFromToken(token);
        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new NotFoundException("autizm")); // <--- autizm / shobi ne rugalsa
        String eventNameDto = eventDto.getName();
        String convertedEventName = eventNameDto.substring(0, 1).toUpperCase() + eventNameDto.substring(1).toLowerCase();
        Optional<Event> optionalUserEvent = eventRepository.findByNameAndUserId(convertedEventName, user);
        if (optionalUserEvent.isPresent()) {
            return createErrorResponse(ErrorEntity.EVENT_ALREADY_CREATED);
        }
        Event event = new Event(convertedEventName, eventDto.getDescription(), eventDto.getDate(),
                eventTypeRepository.findById(eventDto.getEventTypeId())
                        .orElseThrow(NotFoundException::new),
                categoryRepository.findById(eventDto.getCategoryId())
                        .orElseThrow(NotFoundException::new), user);
        eventRepository.save(event);
        return createGoodResponse(eventMapper.toEventOutDtoConvert(event));
    }

    @Override
    public ResponseEntity updateEventById(EventDto eventDto, long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        event.setName(eventDto.getName());
        event.setCategory(categoryRepository.findById(eventDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with id " + eventDto.getCategoryId() + " not found")));
        event.setDate(eventDto.getDate());
        event.setEventType(eventTypeRepository.findById(eventDto.getEventTypeId())
                .orElseThrow(() -> new NotFoundException("Event type with id " + eventDto.getEventTypeId() + " not found")));
        eventRepository.save(event);
        return createGoodResponse("Updated");
    }

    @Override
    public ResponseEntity getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return createGoodResponse(eventMapper.toEventDtoList(events));
    }

    @Override
    public ResponseEntity getEventById(long id) {
        return createGoodResponse(eventMapper.toEventOutDtoConvert(eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"))));
    }

    @Override
    public ResponseEntity deleteEventById(long id) {
        eventRepository.delete(eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found")));
        return createGoodResponse("Deleted");
    }
}
