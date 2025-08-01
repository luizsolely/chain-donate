package com.chaindonate.api.exception;

public class BtcAddressDoesntExistsException extends RuntimeException {
    public BtcAddressDoesntExistsException(String message) {
        super(message);
    }
}
