package com.project.userservice.controllers;

import com.project.userservice.dtos.LoginRequestDto;
import com.project.userservice.dtos.LogoutRequestDto;
import com.project.userservice.dtos.SignupRequestDto;
import com.project.userservice.exceptions.PasswordMismatchException;
import com.project.userservice.exceptions.TokenInvalidException;
import com.project.userservice.exceptions.UserAlreadyExistsException;
import com.project.userservice.exceptions.UserNotFoundException;
import com.project.userservice.models.Token;
import com.project.userservice.models.User;
import com.project.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService)
    {
        this.userService=userService;
    }
    @PostMapping("/signup")
    public User signUp(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {
        String name = signupRequestDto.getName();
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        return userService.signUp(name,email,password);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto loginRequestDto) throws UserNotFoundException, PasswordMismatchException {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        return new ResponseEntity<>(userService.login(email,password),HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) throws TokenInvalidException {
        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
