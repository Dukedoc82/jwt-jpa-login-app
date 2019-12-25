package com.dyukov.taxi.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidOrderDataException extends RuntimeException {

    public InvalidOrderDataException(String message) {
        super(message);
    }
}
