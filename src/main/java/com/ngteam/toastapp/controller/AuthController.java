package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.SignInDto;
import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.services.AuthService;
import com.ngteam.toastapp.services.UserService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import com.ngteam.toastapp.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class AuthController extends ResponseCreator {

    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final AuthService authService;
    private final Validator validator;

    @PostMapping("/sign-up")
    ResponseEntity registrationUser(@RequestBody SignUpDto signUpDto) {
        Optional<ErrorEntity> errorEntity = validator.getUserRegisterFormError(signUpDto);
        if(errorEntity.isPresent()) {
            return createErrorResponse(errorEntity.get());
        } else {
            return createGoodResponse(authService.userRegistration(signUpDto));
        }
    }

    @PostMapping("/sign-in")
    ResponseEntity loginUser(@RequestBody SignInDto signInDto) {
        Optional<ErrorEntity> optionalError = validator.getLoginFormError(signInDto);
        if(optionalError.isPresent()) {
            return createErrorResponse(optionalError.get());
        }
        User user = userService.getByEmail(signInDto.getEmail());
        return createGoodResponse(new TokenDto(jwtHelper.generateToken(user)));
    }
}