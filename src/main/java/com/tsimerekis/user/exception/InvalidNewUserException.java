package com.tsimerekis.user.exception;

public class InvalidNewUserException extends RuntimeException {
    public InvalidNewUserException(String message) {
        super(message);
    }

    public InvalidNewUserException() {
        super();
    }
}
