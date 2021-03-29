package com.ngteam.toastapp.services;

public interface MailService {
    void sendConfirmEmail(String email, String code);
}
