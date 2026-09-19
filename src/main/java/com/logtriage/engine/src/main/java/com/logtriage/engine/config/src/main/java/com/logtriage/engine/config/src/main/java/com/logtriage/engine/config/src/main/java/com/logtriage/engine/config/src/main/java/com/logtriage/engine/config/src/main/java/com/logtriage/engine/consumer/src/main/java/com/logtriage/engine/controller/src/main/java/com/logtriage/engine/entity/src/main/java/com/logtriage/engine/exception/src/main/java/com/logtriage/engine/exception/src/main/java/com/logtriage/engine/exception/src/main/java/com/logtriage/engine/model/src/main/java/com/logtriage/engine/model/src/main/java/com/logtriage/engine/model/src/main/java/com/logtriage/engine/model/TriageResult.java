package com.logtriage.engine.model;

import dev.langchain4j.model.output.structured.Description;

public record TriageResult(

        @Description("Overall severity: LOW, MEDIUM, HIGH, or CRITICAL")
        Severity severity,

        @Description("Short root-cause category, e.g. DATABASE_TIMEOUT, NULL_POINTER, OUT_OF_MEMORY, NETWORK, CONFIG, EXTERNAL_API, UNKNOWN")
        String rootCauseCategory,

        @Description("One or two sentence plain-language summary of what went wrong")
        String summary,

        @Description("A single, concrete, actionable next step for the on-call engineer")
        String suggestedAction,

        @Description("True if this looks like a known/recurring failure pattern rather than a novel issue")
        boolean recurringPattern,

        @Description("Confidence in this triage, from 0.0 to 1.0")
        double confidence
) {
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    public static TriageResult fallback(String reason) {
        return new TriageResult(
                Severity.MEDIUM,
                "UNKNOWN",
                "Automated triage unavailable: " + reason,
                "Route to on-call engineer for manual review",
                false,
                0.0
        );
    }
}
