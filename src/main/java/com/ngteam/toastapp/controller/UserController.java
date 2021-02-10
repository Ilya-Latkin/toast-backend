package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.ProfileUpdateDto;
import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.UserService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController extends ResponseCreator {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final UserService userService;

    @GetMapping
    ResponseEntity getProfile(@RequestHeader String authorization) {
        UserDto dto = userService.getUserProfileInfo(authorization);
        return createGoodResponse(dto);
    }

    @DeleteMapping
    ResponseEntity deleteProfile(@RequestHeader String authorization) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
            userService.delete(user);
            return createGoodResponse("Deleted");
        } else {
            return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
        }
    }

    @PutMapping(path = "/change")
    ResponseEntity changeUserName(@RequestHeader String authorization,@RequestBody ProfileUpdateDto profileUpdateDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
            user.setName(profileUpdateDto.getName());
            userRepository.save(user);
            return createGoodResponse("Name has been changed");
        } else  {
            return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
        }
    }

    @PostMapping(path = "/change")
    ResponseEntity changePassword(@RequestHeader String authorization,@RequestBody ProfileUpdateDto profileUpdateDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            if (profileUpdateDto.getPassword().length() > 5 && profileUpdateDto.getPassword() != null) {
                User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
                 user.setPassword(profileUpdateDto.getPassword());
                return createGoodResponse("Password has been changed");
            } else {
                return createErrorResponse(ErrorEntity.INCORRECT_PASSWORD);
            }
        } else  {
            return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
        }
    }
}