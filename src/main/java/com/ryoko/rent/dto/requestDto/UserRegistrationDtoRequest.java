package com.ryoko.rent.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserRegistrationDtoRequest {

    @NotNull(message = "Адрес почты не может быть пустым.")
    private String email;

    @NotNull(message = "Пароль не может быть пустым.")
    @Min(value = 8, message = "Длина пароля должна быть не менее 8 символов.")
    private String password;

    @NotNull(message = "Имя не может быть пустым.")
    private String name;

    @NotNull(message = "Фамилия не может быть пустой.")
    private String lastname;

    private String patronymic;

    @NotNull(message = "Номер телефона не может быть пустым.")
    private String phone;

}