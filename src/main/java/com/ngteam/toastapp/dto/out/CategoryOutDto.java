package com.ngteam.toastapp.dto.out;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryOutDto {
    private long id;
    private String name;
    private List<String> events;
    private UserDtoOut user;
}
