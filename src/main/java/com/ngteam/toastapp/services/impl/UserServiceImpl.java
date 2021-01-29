package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.exceptions.InvalidTokenException;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    public UserServiceImpl(JwtHelper jwtHelper, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(User user) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UserDto getByAuthToken(String authToken) {
        return null;
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findOneById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean update(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }


    public UserDto getUserProfileInfo(String authorization) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if (token == null || jwtHelper.validateToken(token) == false) {
            throw new InvalidTokenException("Ошибка авторизации");
        }
        String email = jwtHelper.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String tokenPassword = jwtHelper.getPasswordFromToken(token);
            if (tokenPassword.equals(user.getPassword()) == false) {
                throw new InvalidTokenException("Wrong password");
            }
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            return userDto;
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }
    }

}
