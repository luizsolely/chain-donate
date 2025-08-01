package com.chaindonate.api.exception;

import lombok.Value;
import java.time.LocalDateTime;

@Value
public class ErrorResponse {
    LocalDateTime timestamp = LocalDateTime.now();
    int status;
    String error;
    String message;
    String path;
}
