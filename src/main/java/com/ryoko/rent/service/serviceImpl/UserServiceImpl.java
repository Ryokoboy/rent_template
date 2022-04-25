package com.ryoko.rent.service.serviceImpl;

import com.ryoko.rent.dto.requestDto.UserAuthorizationDtoRequest;
import com.ryoko.rent.dto.requestDto.UserChangePasswordDtoRequest;
import com.ryoko.rent.dto.requestDto.UserRegistrationDtoRequest;
import com.ryoko.rent.dto.responseDto.UserDtoResponse;
import com.ryoko.rent.enviroment.JWTEnvironmentBuilder;
import com.ryoko.rent.exception.ExceptionDescription;
import com.ryoko.rent.exception.domain.RepositoryException;
import com.ryoko.rent.mapper.UserMapper;
import com.ryoko.rent.model.User;
import com.ryoko.rent.repository.UserRepository;
import com.ryoko.rent.security.JWTTokenProvider;
import com.ryoko.rent.security.UserPrincipal;
import com.ryoko.rent.service.serviceInterface.EmailService;
import com.ryoko.rent.service.serviceInterface.PinCodeService;
import com.ryoko.rent.service.serviceInterface.RoleService;
import com.ryoko.rent.service.serviceInterface.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;
    private final PinCodeService pinCodeService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder;
    private final JWTEnvironmentBuilder jwtEnvironmentBuilder;
    private final JWTTokenProvider jwtTokenProvider;
    private final EmailService emailService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, @Lazy PinCodeService pinCodeService, AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, JWTEnvironmentBuilder jwtEnvironmentBuilder, JWTTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.pinCodeService = pinCodeService;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtEnvironmentBuilder = jwtEnvironmentBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User getByEmailThrowException(String email) {
        return this.getByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(ExceptionDescription.UsernameNotFoundException, email)));
    }

    @Override
    public User registration(UserRegistrationDtoRequest userRegistrationDtoRequest) {
        User user = new User();

        user.setEmail(userRegistrationDtoRequest.getEmail());
        user.setPassword(this.encoder.encode(userRegistrationDtoRequest.getPassword()));
        user.setName(userRegistrationDtoRequest.getName());
        user.setSurname(userRegistrationDtoRequest.getLastname());
        user.setPhone(userRegistrationDtoRequest.getPhone());

        User createdUser;
        try{
            user.setRole(this.roleService.getByNameThrowException("ROLE_CHALLENGER"));
            createdUser = this.userRepository.save(user);
        }catch (Exception e){
            log.error(e);
            throw new RepositoryException(String
                    .format(ExceptionDescription.RepositoryException, "registering", "user"));
        }
        return createdUser;
    }

    @Override
    public User activate(String email, Integer pinCode) {
        User user = this.pinCodeService.checkPinCode(pinCode, email).getUser();

        user.setActive(true);
        User createdUser;
        try{
            createdUser = this.userRepository.save(user);
        } catch (Exception e) {
            log.error(e);
            throw new RepositoryException(String
                    .format(ExceptionDescription.RepositoryException, "activating", "user"));
        }
        return createdUser;
    }

    @Override
    public ResponseEntity<UserDtoResponse> authorization(UserAuthorizationDtoRequest authorizationDtoRequest, HttpServletRequest request) {
        String email = authorizationDtoRequest.getEmail().toLowerCase().trim();
        String password = authorizationDtoRequest.getPassword().trim();

        this.authenticate(email, password);

        User user = getByEmailThrowException(email);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String ipFromClient = jwtTokenProvider.getIpFromClient(request);
        HttpHeaders jwt = getJwtHeader(userPrincipal, ipFromClient);
        UserDtoResponse userDtoResponse = UserMapper.userToDto(user);
        return new ResponseEntity<>(userDtoResponse, jwt, HttpStatus.OK);

    }

    @Override
    public void changePassword(Principal principal, UserChangePasswordDtoRequest userDto) {
        User user = this.getByEmailThrowException(principal.getName());

        user.setPassword(this.encoder.encode(userDto.getPassword()));

        try {
            this.userRepository.save(user);
        }catch (Exception e){
            log.error(e);
            throw new RepositoryException(String.format(ExceptionDescription.RepositoryException, "changing", "password"));
        }
    }

    @Override
    public void forgotPassword(String email) throws MessagingException {
        User user = this.getByEmailThrowException(email);
        String generatePassword = this.pinCodeService.generatePassword();
        System.out.println(generatePassword);
        user.setPassword(this.encoder.encode(generatePassword));

        try {
            this.userRepository.save(user);
        }catch (Exception e){
            log.error(e);
            throw new RepositoryException(String.format(ExceptionDescription.RepositoryException, "changing", "password"));
        }

        emailService.sendMessage(user.getEmail(), String.format("Hello, dear %s %s, your new password: %s", user.getName(), user.getSurname(), generatePassword));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal, String ipFromClient) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtEnvironmentBuilder.getJWT_TOKEN_HEADER(), jwtTokenProvider.generateToken(userPrincipal, ipFromClient));
        return httpHeaders;
    }


    private void authenticate(String email, String password) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
