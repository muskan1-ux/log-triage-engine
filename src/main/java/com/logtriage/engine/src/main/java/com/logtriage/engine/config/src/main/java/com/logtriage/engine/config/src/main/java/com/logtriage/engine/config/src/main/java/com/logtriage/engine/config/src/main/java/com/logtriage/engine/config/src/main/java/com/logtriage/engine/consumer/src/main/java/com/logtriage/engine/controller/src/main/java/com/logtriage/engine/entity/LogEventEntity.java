package com.logtriage.engine.entity;

import com.logtriage.engine.model.LogLevel;
import com.logtriage.engine.model.TriageResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "log_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEventEntity {

    @Id
    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private String host;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogLevel level;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private TriageResult.Severity severity;

    @Column(name = "root_cause_category")
    private String rootCauseCategory;

    @Column(name = "triage_summary", columnDefinition = "TEXT")
    private String triageSummary;

    @Column(name = "suggested_action", columnDefinition = "TEXT")
    private String suggestedAction;

    @Column(name = "recurring_pattern")
    private Boolean recurringPattern;

    private Double confidence;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
