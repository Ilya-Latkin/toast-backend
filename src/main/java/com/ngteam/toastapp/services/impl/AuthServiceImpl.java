package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.dto.in.SignUpDto;
import com.ngteam.toastapp.dto.in.TokenDto;
import com.ngteam.toastapp.model.State;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
//    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDto userRegistration(SignUpDto signUpDto) {
        User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
//                .state(State.NOT_CONFIRMED)
//                .confirmCode(UUID.randomUUID().toString())
                .userCategory(createDefaultCategories())
                .build();

        userRepository.save(user);
//        mailService.sendConfirmEmail(user.getEmail(), user.getConfirmCode());
        return new TokenDto(jwtHelper.generateToken(user));
    }

    private List<Category> createDefaultCategories() {
        String [] categoryNames = {"Друзья", "Родственники", "Коллеги", "Животные"};
        List<Category> defaultCategories = new ArrayList<>();
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name(categoryNames[i])
                    .build();
            defaultCategories.add(category);
        }
        return  defaultCategories;
    }
}