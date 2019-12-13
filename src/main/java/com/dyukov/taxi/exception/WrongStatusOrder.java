package com.dyukov.taxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class WrongStatusOrder extends RuntimeException {

    private static final long serialVersionUID = -6549591936158151203L;

    public WrongStatusOrder(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongStatusOrder(String message) {
        super(message);
    }

}
