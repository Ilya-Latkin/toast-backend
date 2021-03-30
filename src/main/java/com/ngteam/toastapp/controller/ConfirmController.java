package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.State;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ConfirmController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/confirm/{code}")
    public String confirmUser(@PathVariable("code") String code){
        User user = userRepository.findByConfirmCode(code)
                .orElseThrow(() -> new NotFoundException("User with this confirm code not found"));
        user.setState(State.CONFIRMED);
        userRepository.save(user);
        return "sucess_confirm";
    }
}
