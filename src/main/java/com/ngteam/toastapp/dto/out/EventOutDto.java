package com.ngteam.toastapp.dto.out;

import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EventOutDto {
    private long id;
    private String name;
    private String description;
    private Date date;
    private EventTypeDto eventType;
    private CategoryDto category;
    private UserDtoOut user;
}