package com.ngteam.toastapp.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorEntity {
    // Общие ошибки
    INVALID_REQUEST(400, "Неверный запрос"),
    INVALID_TOKEN_TOKEN(403, "Ошибка авторизации"),
    NOT_FOUND(404, "Не найдено"),
    INVALID_NAME(450, "Неверное имя"),
    EMAIL_ALREADY_TAKEN(453, "Email уже занят"),
    CATEGORY_ALREADY_CREATED(454, "Категория уже создана"),
    EVENT_TYPE_ALREADY_CREATED(454, "Тип события уже создан"),
    EVENT_ALREADY_CREATED (454, "Событие уже создано"),

    // Регистрация
    DUPLICATE_PASSWORD(460, "Пароль уже используется"),
    PASSWORD_TOO_SHORT(460, "Пароль слишком короткий"),
    INVALID_EMAIL(461, "Некорректный Email"),

    // Вход
    USER_NOT_FOUND(404,"Пользователь не найден"),
    INCORRECT_PASSWORD(460, "Неверный пароль");

    int status;
    String message;

    @JsonIgnore
    Logger log = LoggerFactory.logger(ErrorEntity.class);

    ErrorEntity(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void log() {
        log.error("Ошибка " + status + ": " + message);
    }
}
