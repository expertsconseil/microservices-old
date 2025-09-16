package com.expertsconseil.user_microservice.config.exception;

public class UserDataException extends RuntimeException {
    private final String message;

    public UserDataException(String s) {
        super(s);
        this.message = s;
    }
}
