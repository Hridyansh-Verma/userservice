package com.project.userservice.exceptions;

public class TokenInvalidException extends Exception{
    public TokenInvalidException(String message) {
        super(message);
    }
}
