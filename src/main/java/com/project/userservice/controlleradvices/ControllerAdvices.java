package com.project.userservice.controlleradvices;

import com.project.userservice.dtos.ExceptionDto;
import com.project.userservice.exceptions.PasswordMismatchException;
import com.project.userservice.exceptions.TokenInvalidException;
import com.project.userservice.exceptions.UserAlreadyExistsException;
import com.project.userservice.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException)
    {
        ExceptionDto exceptionDto= new ExceptionDto();
        exceptionDto.setMessage(userAlreadyExistsException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleUserNotFoundException(UserNotFoundException userNotFoundException)
    {
        ExceptionDto exceptionDto= new ExceptionDto();
        exceptionDto.setMessage(userNotFoundException.getMessage());
        return new ResponseEntity<>(exceptionDto,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ExceptionDto> handlePasswordMismatchException(PasswordMismatchException passwordMismatchException)
    {
        ExceptionDto exceptionDto= new ExceptionDto();
        exceptionDto.setMessage(passwordMismatchException.getMessage());
        return new ResponseEntity<>(exceptionDto,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ExceptionDto> handleTokenInvalidException(TokenInvalidException tokenInvalidException)
    {
        ExceptionDto exceptionDto= new ExceptionDto();
        exceptionDto.setMessage(tokenInvalidException.getMessage());
        return new ResponseEntity<>(exceptionDto,HttpStatus.NOT_FOUND);
    }
}
