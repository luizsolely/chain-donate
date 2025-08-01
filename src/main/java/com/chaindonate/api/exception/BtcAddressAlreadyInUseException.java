package com.chaindonate.api.exception;

public class BtcAddressAlreadyInUseException extends RuntimeException {
    public BtcAddressAlreadyInUseException(String message) {
        super(message);
    }
}
