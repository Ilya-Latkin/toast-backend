package com.ngteam.toastapp.dto.mapper;

import com.ngteam.toastapp.dto.out.UserDtoOut;
import com.ngteam.toastapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDtoOut toUserDtoOut(User user) {
        UserDtoOut userDtoOut = new UserDtoOut();
        userDtoOut.setName(user.getName());
        return userDtoOut;
    }
}