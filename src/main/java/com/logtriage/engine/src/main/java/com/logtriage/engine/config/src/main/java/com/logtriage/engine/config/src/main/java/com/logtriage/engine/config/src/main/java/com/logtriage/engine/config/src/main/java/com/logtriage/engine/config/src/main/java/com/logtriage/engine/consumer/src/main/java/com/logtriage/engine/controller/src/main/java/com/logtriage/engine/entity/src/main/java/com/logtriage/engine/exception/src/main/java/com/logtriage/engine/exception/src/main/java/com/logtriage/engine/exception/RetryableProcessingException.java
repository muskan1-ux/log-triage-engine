package com.logtriage.engine.exception;

public class RetryableProcessingException extends LogProcessingException {
    public RetryableProcessingException(String message) {
        super(message);
    }

    public RetryableProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
