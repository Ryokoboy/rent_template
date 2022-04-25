package com.ryoko.rent.service.serviceInterface;

import com.ryoko.rent.model.User;

import javax.mail.MessagingException;

public interface EmailService {

    void sendVerificationPinCode(User user, int pinCode) throws MessagingException;

    void sendMessage(String email, String text) throws MessagingException;
}
