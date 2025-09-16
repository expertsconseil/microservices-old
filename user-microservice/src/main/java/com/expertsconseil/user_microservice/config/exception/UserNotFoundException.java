package com.expertsconseil.user_microservice.config.exception;

public class UserNotFoundException extends RuntimeException {

    private final String message;
    private Throwable ex;

    public UserNotFoundException(String message, Throwable ex) {
        super(message, ex);
        this.message = message;
        this.ex = ex;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
