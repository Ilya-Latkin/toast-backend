package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;
import com.ngteam.toastapp.model.State;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public TokenDto userRegistration(SignUpDto signUpDto) {
        User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .state(State.NOT_CONFIRMED)
                .build();
        userRepository.save(user);
        return new TokenDto(jwtHelper.generateToken(user));
    }
}