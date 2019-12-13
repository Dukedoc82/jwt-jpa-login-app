package com.dyukov.taxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3245349694062898898L;

    public UserNotFoundException(String message) {
        super(message);
    }

    private UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Long userId) {
        this(String.format(TaxiServiceException.USER_ID_DOES_NOT_EXIST, userId));
    }

    public UserNotFoundException(Long userId, Throwable cause) {
        this(String.format(TaxiServiceException.USER_ID_DOES_NOT_EXIST, userId), cause);
    }
}
