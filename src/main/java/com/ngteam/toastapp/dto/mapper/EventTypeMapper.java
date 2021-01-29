package com.ngteam.toastapp.dto.mapper;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import com.ngteam.toastapp.dto.out.EventTypeOutDto;
import com.ngteam.toastapp.model.EventType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventTypeMapper {

    @Autowired
    EventMapper eventMapper;

    @Autowired
    JwtHelper jwtHelper;

    public EventType toEventTypeConverter(EventTypeDto eventTypeDto) {
        return toEventTypeConverter(eventTypeDto, new EventType());
    }

    public EventType toEventTypeConverter(EventTypeDto eventTypeDto, EventType eventType) {
        eventType.setId(eventType.getId());
        eventType.setName(eventType.getName());
        return eventType;
    }

    public EventTypeOutDto toEventTypeOutDtoConvert(EventType eventType) {
        EventTypeOutDto eventTypeOutDto = new EventTypeOutDto();
        eventTypeOutDto.setId(eventType.getId());
        eventTypeOutDto.setName(eventType.getName());
        eventTypeOutDto.setUser(eventType.getUser().getName());
        eventTypeOutDto.setEvents(
                eventType.getEvents()
                        .stream()
                        .map(eventMapper::toEventOutDtoConvert)
                        .collect(Collectors.toList())
        );
        return eventTypeOutDto;

    }

    public EventTypeDto toEventTypeDtoConvert(EventType eventType) {
        EventTypeDto eventTypeDto = new EventTypeDto();
        eventTypeDto.setId(eventType.getId());
        eventTypeDto.setName(eventType.getName());
        return eventTypeDto;
    }

    public List<EventType> toEventTypeList(List<EventTypeDto> eventTypeDtos) {
        return eventTypeDtos
                .stream()
                .map(this::toEventTypeConverter)
                .collect(Collectors.toList());
    }

    public List<EventTypeOutDto> toEventTypeDtoList(List<EventType> eventTypes) {
        return eventTypes
                .stream()
                .map(this::toEventTypeOutDtoConvert)
                .collect(Collectors.toList());
    }
}
