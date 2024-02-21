package com.project.userservice.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.project.userservice.exceptions.PasswordMismatchException;
import com.project.userservice.exceptions.TokenInvalidException;
import com.project.userservice.exceptions.UserAlreadyExistsException;
import com.project.userservice.exceptions.UserNotFoundException;
import com.project.userservice.models.Token;
import com.project.userservice.models.User;
import com.project.userservice.repositories.TokenRepository;
import com.project.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flywaydb.core.internal.logging.apachecommons.ApacheCommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepository userRepository,TokenRepository tokenRepository,BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.tokenRepository=tokenRepository;
    }
    public User signUp(String name,String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent())
        {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user= new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Token login(String email,String password) throws UserNotFoundException, PasswordMismatchException
    {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty())
        {
            throw new UserNotFoundException("User not found");
        }
        User user=userOptional.get();
        if(bCryptPasswordEncoder.matches(password, user.getHashedPassword()))
        {
            Token token = new Token();
            token.setUser(user);
            LocalDate localDate= LocalDate.now().plus(30,java.time.temporal.ChronoUnit.DAYS);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            // Convert LocalDateTime to Date
            Date date = Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
            token.setExpiryAt(date);
            token.setValue(RandomStringUtils.randomAlphanumeric(128));
            return tokenRepository.save(token);
        } else {
            throw new PasswordMismatchException("Password Mismatch");
        }
    }

    public void logout(String token) throws TokenInvalidException {
        Optional<Token> tokenOptional = tokenRepository.findByValue(token);
        if(tokenOptional.isPresent())
        {
            if(tokenOptional.get().getIsDeleted()==true)
            {
                throw new TokenInvalidException("Token is already deleted");
            }
            tokenOptional.get().setIsDeleted(true);
            tokenRepository.save(tokenOptional.get());
        }
        else {
            throw new TokenInvalidException("Token is invalid");
        }
    }
    public User validateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndIsDeletedEqualsAndExpiryAtGreaterThan(token,false,new Date());
        if(tokenOptional.isPresent())
        {
            return tokenOptional.get().getUser();
        }
        return null;
    }
}
