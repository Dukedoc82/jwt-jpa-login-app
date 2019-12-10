package com.dyukov.taxi.exception;

public class TaxiServiceException extends RuntimeException {

    private int code;

    public TaxiServiceException(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
