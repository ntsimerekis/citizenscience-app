package com.tsimerekis.user.exception;

public class UserAlreadyExistsException extends InvalidNewUserException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
