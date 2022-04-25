package com.ryoko.rent.service.serviceInterface;

import com.ryoko.rent.dto.requestDto.UserAuthorizationDtoRequest;
import com.ryoko.rent.dto.requestDto.UserChangePasswordDtoRequest;
import com.ryoko.rent.dto.requestDto.UserRegistrationDtoRequest;
import com.ryoko.rent.dto.responseDto.UserDtoResponse;
import com.ryoko.rent.model.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
import javax.mail.MessagingException;

public interface UserService {

    Optional<User> getByEmail(String email);

    User getByEmailThrowException(String email);

    User registration(UserRegistrationDtoRequest userRegistrationDtoRequest);

    User activate(String email, Integer pinCode);

    ResponseEntity<UserDtoResponse> authorization(UserAuthorizationDtoRequest authorizationDtoRequest, HttpServletRequest request);

    void changePassword(Principal principal, UserChangePasswordDtoRequest userDto);

    void forgotPassword(String email) throws MessagingException;
}

