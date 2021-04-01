package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.dto.in.ProfileUpdateDto;
import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.services.UserService;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController extends ResponseCreator {

    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    ResponseEntity getProfile(@RequestHeader String authorization) {
        UserDto dto = userService.getUserProfileInfo(authorization);
        return createGoodResponse(dto);
    }

    @DeleteMapping
    ResponseEntity deleteProfile(@RequestHeader String authorization) {
        String token = JwtHelper.getTokenFromHeader(authorization);
            User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
            userService.delete(user);
            return createGoodResponse("Deleted");
    }

    @PutMapping(path = "/change")
    ResponseEntity changeUserName(@RequestHeader String authorization,@RequestBody ProfileUpdateDto profileUpdateDto) {
        return userService.changeUserName(authorization, profileUpdateDto);
    }

    @PostMapping(path = "/change")
    ResponseEntity changePassword(@RequestHeader String authorization,@RequestBody ProfileUpdateDto profileUpdateDto) {
        return userService.changePassword(authorization, profileUpdateDto);
    }
}