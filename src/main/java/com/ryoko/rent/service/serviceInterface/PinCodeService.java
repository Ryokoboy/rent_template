package com.ryoko.rent.service.serviceInterface;

import com.ryoko.rent.model.PinCode;

import java.util.Optional;

public interface PinCodeService {

    void create(String email);

    PinCode checkPinCode(Integer pinCode, String email);

    Optional<PinCode> getByUserEmail(String email);

    PinCode getByUserEmailThrowException(String email);

    String generatePassword();
}
