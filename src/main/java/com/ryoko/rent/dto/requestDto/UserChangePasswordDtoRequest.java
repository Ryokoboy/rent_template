package com.ryoko.rent.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserChangePasswordDtoRequest {

    @NotNull(message = "Пароль не может быть пустым.")
    @Min(value = 8, message = "Длина пароля должна быть не менее 8 символов.")
    private String password;
}
