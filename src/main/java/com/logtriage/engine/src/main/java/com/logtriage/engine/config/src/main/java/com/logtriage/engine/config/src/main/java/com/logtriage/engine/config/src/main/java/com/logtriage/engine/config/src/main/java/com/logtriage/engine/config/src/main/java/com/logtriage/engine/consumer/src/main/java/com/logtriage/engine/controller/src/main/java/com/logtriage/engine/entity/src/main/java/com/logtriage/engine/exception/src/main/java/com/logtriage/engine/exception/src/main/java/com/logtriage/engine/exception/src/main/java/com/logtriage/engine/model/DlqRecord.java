package com.logtriage.engine.model;

import java.time.Instant;

public record DlqRecord(
        LogEvent originalEvent,
        String exceptionType,
        String exceptionMessage,
        Instant failedAt
) {}
