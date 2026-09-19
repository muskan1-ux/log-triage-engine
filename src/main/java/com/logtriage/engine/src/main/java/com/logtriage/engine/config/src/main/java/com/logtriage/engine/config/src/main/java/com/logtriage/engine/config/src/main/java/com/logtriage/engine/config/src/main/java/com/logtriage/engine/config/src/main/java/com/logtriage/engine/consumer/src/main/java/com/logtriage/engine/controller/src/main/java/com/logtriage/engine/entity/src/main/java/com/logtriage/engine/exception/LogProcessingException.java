package com.logtriage.engine.exception;

public abstract class LogProcessingException extends RuntimeException {
    protected LogProcessingException(String message) {
        super(message);
    }

    protected LogProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
