package com.ecom.exception;

import com.ecom.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerUserNotFoundException(UserNotFoundException ex){
        return ErrorResponse.builder()
                .error("User not found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerUserNotVerifiedException(UserNotVerifiedException ex){
        return ErrorResponse.builder()
                .error(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserAlreadyExist(UserAlreadyExistsException ex){
        return ErrorResponse.builder()
                .error("User already exist")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidResetPasswordToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerInvalidResetPasswordToken(InvalidResetPasswordToken ex){
        return ErrorResponse.builder()
                .error("Password reset token is invalid")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerEmailNotFound(EmailNotFoundException ex){
        return ErrorResponse.builder()
                .error("E-mail not found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(EmailFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerEmailFailure(EmailFailureException ex){
        return ErrorResponse.builder()
                .error("E-mail sending failed")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalServerError(Exception ex){
        return ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .error("Internal Conflict")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
