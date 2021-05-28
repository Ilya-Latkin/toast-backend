package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.services.ConfirmationService;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/confirm")
@Controller
@AllArgsConstructor
public class ConfirmController extends ResponseCreator {

    private final ConfirmationService confirmationService;
    private final JwtHelper jwtHelper;

    @GetMapping("/{code}")
    public String confirmUser(@PathVariable("code") String code){
        return confirmationService.confirmAccount(code);
    }

    @GetMapping("/sendcode")
    ResponseEntity sendConfirmCode(@RequestHeader String authorization) {
        return createGoodResponse(confirmationService.sendCode(jwtHelper.getUserFromHeader(authorization)));
    }
}
