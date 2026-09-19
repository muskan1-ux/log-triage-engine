package com.logtriage.engine.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record LogEvent(

        @NotBlank String eventId,
        @NotBlank String service,
        @NotBlank String host,
        @NotNull LogLevel level,
        @NotBlank String message,
        String stackTrace,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant timestamp
) {
    @JsonCreator
    public LogEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("service") String service,
            @JsonProperty("host") String host,
            @JsonProperty("level") LogLevel level,
            @JsonProperty("message") String message,
            @JsonProperty("stackTrace") String stackTrace,
            @JsonProperty("timestamp") Instant timestamp) {
        this.eventId = eventId;
        this.service = service;
        this.host = host;
        this.level = level;
        this.message = message;
        this.stackTrace = stackTrace;
        this.timestamp = timestamp == null ? Instant.now() : timestamp;
    }
}
