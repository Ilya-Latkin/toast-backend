package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.services.ConfirmationService;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/confirm")
@Controller
@AllArgsConstructor
public class ConfirmController extends ResponseCreator {

    private final ConfirmationService confirmationService;

    @GetMapping("/{code}")
    public String confirmUser(@PathVariable("code") String code){
        return confirmationService.confirmAccount(code);
    }
}
