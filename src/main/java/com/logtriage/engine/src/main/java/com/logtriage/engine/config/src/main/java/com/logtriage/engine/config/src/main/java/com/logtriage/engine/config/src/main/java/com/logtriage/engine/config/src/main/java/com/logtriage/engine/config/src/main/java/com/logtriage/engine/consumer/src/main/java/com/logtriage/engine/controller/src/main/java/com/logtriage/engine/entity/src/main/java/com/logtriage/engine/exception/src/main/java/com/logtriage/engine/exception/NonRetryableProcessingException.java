package com.logtriage.engine.exception;

public class NonRetryableProcessingException extends LogProcessingException {
    public NonRetryableProcessingException(String message) {
        super(message);
    }

    public NonRetryableProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
