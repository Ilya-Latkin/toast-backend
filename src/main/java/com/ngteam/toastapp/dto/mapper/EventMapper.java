package com.ngteam.toastapp.dto.mapper;

import com.ngteam.toastapp.dto.in.EventDto;
import com.ngteam.toastapp.dto.out.EventOutDto;
import com.ngteam.toastapp.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventMapper {

    @Autowired
    EventTypeMapper eventTypeMapper;

    @Autowired
    CategoryMapper categoryMapper;

    public Event toEventConverter(EventDto eventDto) {
        return toEventConverter(eventDto, new Event());
    }

    public Event toEventConverter(EventDto eventDto, Event event) {
        event.setId(event.getId());
        event.setName(event.getName());
        return event;
    }

    public EventOutDto toEventOutDtoConvert(Event event) {
        EventOutDto eventOutDto = new EventOutDto();
        eventOutDto.setId(event.getId());
        eventOutDto.setName(event.getName());
        eventOutDto.setDescription(event.getDescription());
        eventOutDto.setDate(event.getDate());
        eventOutDto.setEventType(eventTypeMapper.toEventTypeDtoConvert(event.getEventType()));
        eventOutDto.setCategory(categoryMapper.toCategoryDtoConvert(event.getCategory()));
        return eventOutDto;
    }

/*
    public EventTypeOutDto toEventTypeOutDtoConvert(EventType eventType, boolean flag) {
        EventTypeOutDto eventTypeOutDto = new EventTypeOutDto();
        eventTypeOutDto.setId(eventType.getId());
        eventTypeOutDto.setName(eventType.getName());
        if(flag == true) {
            eventTypeOutDto.setEvents(
                    eventType.getEvents()
                            .stream()
                            .map(event -> event.getName() + " id " + event.getId())
                            .collect(Collectors.toList())
            );
        }
        return eventTypeOutDto;
    }
*/

    public List<Event> toEventList(List<EventDto> eventDtos) {
        return eventDtos
                .stream()
                .map(this::toEventConverter)
                .collect(Collectors.toList());
    }

    public List<EventOutDto> toEventDtoList(List<Event> events) {
        return events
                .stream()
                .map(this::toEventOutDtoConvert)
                .collect(Collectors.toList());
    }
}

