package com.project.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.dtos.SendEmailEventDto;
import com.project.userservice.exceptions.TokenInvalidException;
import com.project.userservice.models.Token;
import com.project.userservice.models.User;
import com.project.userservice.repositories.TokenRepository;
import com.project.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String,String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       TokenRepository tokenRepository,
                       KafkaTemplate<String, String> kafkaTemplate,
                       ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signUp(String fullName,
                       String email,
                       String password) {
        User u = new User();
        u.setEmail(email);
        u.setName(fullName);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));

        User user = userRepository.save(u);
        SendEmailEventDto sendEmailEventDto=new SendEmailEventDto();
        sendEmailEventDto.setTo(email);
        sendEmailEventDto.setFrom("hridyanshverma99@gmail.com");
        sendEmailEventDto.setSubject("Welcome to our platform");
        sendEmailEventDto.setBody("Welcome to our platform. Nice to have you here.");
        try {
            kafkaTemplate.send("sendEmail",objectMapper.writeValueAsString(sendEmailEventDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public Token login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // throw user not exists exception
            return null;
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            // throw password not matching exception
            return null;
        }

        Token token = getToken(user);

        // TODO 1: Change the above token to a JWT Token

        Token savedToken = tokenRepository.save(token);

        return savedToken;
    }

    private static Token getToken(User user) {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        return token;
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
        Optional<Token> tokenOptional = tokenRepository.
                findByValueAndIsDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());

        if (tokenOptional.isEmpty()) {
            return null;
        }

        // TODO 2: Instead of validating via the DB, as the token is now a JWT
        // token, validate using JWT

        return tokenOptional.get().getUser();
    }
}