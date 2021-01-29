package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper jwtHelper;

    @Override
    public TokenDto userRegistration(SignUpDto signUpDto) {
        User user = new User(signUpDto.getName(), signUpDto.getEmail(), signUpDto.getPassword());
        userRepository.save(user);
        return new TokenDto(jwtHelper.generateToken(user));
    }
}