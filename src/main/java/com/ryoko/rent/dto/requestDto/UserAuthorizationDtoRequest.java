package com.ryoko.rent.dto.requestDto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UserAuthorizationDtoRequest {

    @NotNull(message = "Адрес почты не может быть пустым.")
    private String email;

    @NotNull(message = "Пароль не может быть пустым.")
    private String password;
}
