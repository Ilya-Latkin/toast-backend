package com.ngteam.toastapp.services;

import com.ngteam.toastapp.model.ConfirmationToken;
import com.ngteam.toastapp.model.User;

public interface ConfirmationService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    String confirmAccount(String code);
    String sendCode(User user);
}