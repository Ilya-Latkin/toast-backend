package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.dto.in.ProfileUpdateDto;
import com.ngteam.toastapp.dto.in.UserDto;
import com.ngteam.toastapp.exceptions.InvalidTokenException;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.UserService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends ResponseCreator implements UserService {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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

    @Override
    public ResponseEntity changePassword(String authorization, ProfileUpdateDto profileUpdateDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if (profileUpdateDto.getPassword().length() > 5 && profileUpdateDto.getPassword() != null) {
            User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
            if (profileUpdateDto.getPassword().equals(user.getPassword())) {
                return createErrorResponse(ErrorEntity.DUPLICATE_PASSWORD);
            } else {
                user.setPassword(profileUpdateDto.getPassword());
                userRepository.save(user);
                return createGoodResponse("Password has been changed");
            }
        } else {
            return createErrorResponse(ErrorEntity.INCORRECT_PASSWORD);
        }
    }

    @Override
    public ResponseEntity changeUserName(String authorization, ProfileUpdateDto profileUpdateDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        User user = userService.getByEmail(jwtHelper.getEmailFromToken(token));
        user.setName(profileUpdateDto.getName());
        userRepository.save(user);
        return createGoodResponse("Name has been changed");
    }

}
