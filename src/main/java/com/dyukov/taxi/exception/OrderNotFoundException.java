package com.dyukov.taxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7823479528648205787L;

    public OrderNotFoundException(String message) {
        super(message);
    }

    private OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Long orderId) {
        this(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST, orderId));
    }

    public OrderNotFoundException(Long orderId, Throwable cause) {
        this(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST, orderId), cause);
    }

}
