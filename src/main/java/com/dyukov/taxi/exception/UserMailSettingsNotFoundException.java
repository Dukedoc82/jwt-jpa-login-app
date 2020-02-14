package com.dyukov.taxi.exception;

public class UserMailSettingsNotFoundException extends RuntimeException {

    public UserMailSettingsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
