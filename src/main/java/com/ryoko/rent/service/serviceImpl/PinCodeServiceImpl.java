package com.ryoko.rent.service.serviceImpl;

import com.ryoko.rent.exception.ExceptionDescription;
import com.ryoko.rent.exception.domain.CustomException;
import com.ryoko.rent.exception.domain.CustomNotFoundException;
import com.ryoko.rent.exception.domain.RepositoryException;
import com.ryoko.rent.model.PinCode;
import com.ryoko.rent.model.User;
import com.ryoko.rent.repository.PinCodeRepository;
import com.ryoko.rent.service.serviceInterface.EmailService;
import com.ryoko.rent.service.serviceInterface.PinCodeService;
import com.ryoko.rent.service.serviceInterface.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PinCodeServiceImpl implements PinCodeService {

    private final PinCodeRepository pinCodeRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public PinCodeServiceImpl(PinCodeRepository pinCodeRepository, UserService userService, EmailService emailService) {
        this.pinCodeRepository = pinCodeRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public Optional<PinCode> getByUserEmail(String email) {
        return this.pinCodeRepository.findByUserEmail(email);
    }

    @Override
    public PinCode getByUserEmailThrowException(String email) {
        return this.getByUserEmail(email)
                .orElseThrow(() -> new CustomNotFoundException(
                        String.format(ExceptionDescription.CustomNotFoundException, "Registration pin code", "users email", email)));
    }

    @Override
    public void create(String email) {
        User user = this.userService.getByEmailThrowException(email);
        Optional<PinCode> registrationPinCode = this.getByUserEmail(email);

        int x = (int) ((Math.random()*((9999-1000)+1))+1000);

        if (registrationPinCode.isPresent()) {
            this.manipulate(registrationPinCode.get(), user, x, true);
        }
        else {
            PinCode newPinCode = new PinCode();
            this.manipulate(newPinCode, user, x, false);
        }
    }

    private void manipulate(PinCode registrationPinCode, User user, int pinCode, boolean exist) {
        if (!exist) registrationPinCode.setUser(user);
        registrationPinCode.setPinCode(pinCode);

        try {
            this.pinCodeRepository.save(registrationPinCode);
            this.emailService.sendVerificationPinCode(user, pinCode);
        } catch (Exception e) {
            log.error(e);
            throw new RepositoryException(String
                    .format(ExceptionDescription.RepositoryException, "creating", "pin code for registration"));
        }
    }

    @Override
    public PinCode checkPinCode(Integer pinCode, String email) {
        this.deleteInvalidPinCode();

        return this.pinCodeRepository.findByPinCodeAndUserEmail(pinCode, email)
                .orElseThrow(() -> new CustomException("Пин код не валидный, попробуйте еще раз."));
    }

    private void deleteInvalidPinCode() {
        List<PinCode> pinCodeList = this.pinCodeRepository.findAll();
        pinCodeList = pinCodeList.stream().filter(v -> v.getCreatedDate().plusMinutes(30).isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        try{
            this.pinCodeRepository.deleteAll(pinCodeList);
        }
        catch (Exception e){
            log.error(e);
        }
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        String ABC = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        String characters = "!@#$%&";

        for (int i = 0; i < 4; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 51 + 1);
            password.append(ABC.charAt(randomNum));
        }

        for (int i = 0; i < 4; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 7 + 1);
            password.append(characters.charAt(randomNum));
        }

        for (int i = 0; i < 4; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 9 + 1);
            password.append(randomNum);
        }

        return String.valueOf(password);
    }
}
