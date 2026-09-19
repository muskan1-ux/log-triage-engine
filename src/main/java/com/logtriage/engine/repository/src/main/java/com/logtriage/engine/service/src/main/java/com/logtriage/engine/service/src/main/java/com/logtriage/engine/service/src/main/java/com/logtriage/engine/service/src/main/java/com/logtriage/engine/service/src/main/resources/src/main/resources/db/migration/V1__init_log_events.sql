CREATE TABLE log_events (
    event_id            VARCHAR(64)  PRIMARY KEY,
    service             VARCHAR(128) NOT NULL,
    host                VARCHAR(128) NOT NULL,
    level               VARCHAR(16)  NOT NULL,
    message             TEXT         NOT NULL,
    stack_trace         TEXT,
    timestamp           TIMESTAMPTZ  NOT NULL,

    severity            VARCHAR(16),
    root_cause_category VARCHAR(64),
    triage_summary      TEXT,
    suggested_action    TEXT,
    recurring_pattern   BOOLEAN,
    confidence          DOUBLE PRECISION,

    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_log_events_service_timestamp ON log_events (service, timestamp DESC);
CREATE INDEX idx_log_events_level ON log_events (level);
CREATE INDEX idx_log_events_severity ON log_events (severity) WHERE severity IS NOT NULL;
