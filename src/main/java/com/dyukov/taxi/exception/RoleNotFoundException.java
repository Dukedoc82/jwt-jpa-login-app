package com.dyukov.taxi.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
