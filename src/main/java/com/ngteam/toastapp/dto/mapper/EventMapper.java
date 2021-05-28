package com.ngteam.toastapp.dto.mapper;

import com.ngteam.toastapp.dto.in.EventDto;
import com.ngteam.toastapp.dto.out.EventOutDto;
import com.ngteam.toastapp.model.Event;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    @Autowired
    EventTypeMapper eventTypeMapper;

    @Autowired
    UserMapper userMapper;

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

    @SneakyThrows
    public EventOutDto toEventOutDtoConvert(Event event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = sdf.format(event.getDate());
        EventOutDto eventOutDto = EventOutDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .date(stringDate)
                .category(categoryMapper.toCategoryDtoConvert(event.getCategory()))
                .eventType(eventTypeMapper.toEventTypeDtoConvert(event.getEventType()))
                .user(userMapper.toUserDtoOut(event.getUser()))
                .participants(userMapper.toUserDtoList(event.getUsers()))
                .build();
        return eventOutDto;
    }

    public List<Event> toEventList(List<EventDto> eventDtos) {
        return eventDtos
                .stream()
                .map(this::toEventConverter)
                .collect(Collectors.toList());
    }

    public List<EventOutDto> toEventDtoList(List<Event> events) {
        System.out.println(events
                .stream()
                .map(this::toEventOutDtoConvert)
                .collect(Collectors.toList()));
        return events
                .stream()
                .map(this::toEventOutDtoConvert)
                .collect(Collectors.toList());
    }
}

