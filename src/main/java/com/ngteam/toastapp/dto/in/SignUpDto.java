package com.ngteam.toastapp.dto.in;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDto {
    private String name;
    private String email;
    private String password;
}
