package com.dyukov.taxi.exception.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidUserDataException extends RuntimeException {

    private Logger logger = LoggerFactory.getLogger(InvalidUserDataException.class);

    public InvalidUserDataException(String message) {

        super(message);
        logger.error(message);
    }

}
