package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;

public interface AuthService {
    TokenDto userRegistration(SignUpDto signUpDto);
}
