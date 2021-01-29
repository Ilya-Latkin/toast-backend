package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.model.User;

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
}
