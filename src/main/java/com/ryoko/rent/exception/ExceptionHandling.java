package com.ryoko.rent.exception;

import com.ryoko.rent.exception.domain.CustomException;
import com.ryoko.rent.exception.domain.CustomNotFoundException;
import com.ryoko.rent.exception.domain.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String INCORRECT_CREDENTIALS = "Email or password incorrect. Please try again";

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<HttpResponseException> RepositoryException(RepositoryException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<HttpResponseException> CustomNotFoundException(CustomNotFoundException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponseException> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<HttpResponseException> CustomException(CustomException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponseException> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponseException> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponseException> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        if (exception.hasErrors()) {
            LOGGER.error(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
            return createHttpResponse(BAD_REQUEST, exception.getFieldError().getDefaultMessage());
        }
        return null;
    }

    private ResponseEntity<HttpResponseException> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponseException(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}
