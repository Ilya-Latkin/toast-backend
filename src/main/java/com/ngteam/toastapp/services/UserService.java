package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.ProfileUpdateDto;
import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    UserDto save(User user);

    User getByEmail(String email);

    UserDto getByAuthToken(String authToken);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneById(Long id);

    boolean update(User user);

    boolean delete(User user);

    UserDto getUserProfileInfo(String authorization);

    ResponseEntity changePassword(String authorization, ProfileUpdateDto profileUpdateDto);

    ResponseEntity changeUserName(String authorization, ProfileUpdateDto profileUpdateDto);
}
