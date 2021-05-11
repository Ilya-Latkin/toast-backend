package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.ConfirmationToken;
import com.ngteam.toastapp.model.State;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.ConfirmationTokenRepository;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.ConfirmationService;
import com.ngteam.toastapp.services.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    @Override
    public String confirmAccount(String code) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(code)
                .orElseThrow(NotFoundException::new);
        LocalDateTime expired = confirmationToken.getExpired();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expired)) {
            confirmationTokenRepository.delete(confirmationToken);
            return "invalid_code";
        }
            confirmationToken.setConfirmed(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            User user = userRepository.findByConfirmToken(code)
                    .orElseThrow(NotFoundException::new);
            user.setState(State.CONFIRMED);
            userRepository.save(user);
            return "success_confirm";
    }

    @Override
    public String sendCode(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15L), user);
        saveConfirmationToken(confirmationToken);
        mailService.sendConfirmEmail(user.getEmail(), token);
        return token;
    }
}
