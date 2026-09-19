package com.logtriage.engine.model;

public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

    public boolean requiresTriage() {
        return this == ERROR || this == FATAL;
    }
}
