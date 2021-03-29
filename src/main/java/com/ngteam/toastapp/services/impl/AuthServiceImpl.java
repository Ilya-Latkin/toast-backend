package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;
import com.ngteam.toastapp.model.State;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.AuthService;
import com.ngteam.toastapp.services.MailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final MailService mailService;

    @Override
    public TokenDto userRegistration(SignUpDto signUpDto) {
        User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .state(State.NOT_CONFIRMED)
                .confirmCode(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);
        mailService.sendConfirmEmail(user.getEmail(), user.getConfirmCode());
        return new TokenDto(jwtHelper.generateToken(user));
    }
}