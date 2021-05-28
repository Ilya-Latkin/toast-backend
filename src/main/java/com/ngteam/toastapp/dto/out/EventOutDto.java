package com.ngteam.toastapp.dto.out;

import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.in.EventTypeDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutDto {
    private long id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;
    private EventTypeDto eventType;
    private CategoryDto category;
    private UserDtoOut user;
    private List<UserDtoOut> participants;
}