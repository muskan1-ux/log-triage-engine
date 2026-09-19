package com.logtriage.engine.service;

import com.logtriage.engine.model.TriageResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface LogTriageService {

    @SystemMessage("""
            You are an expert Site Reliability Engineer performing automated triage on
            application error logs. Analyze the log entry and return a structured
            assessment. Be concise, technical, and decisive. Never invent details that
            are not supported by the log content provided.
            """)
    @UserMessage("""
            Service: {{service}}
            Host: {{host}}
            Level: {{level}}
            Message: {{message}}
            Stack trace:
            {{stackTrace}}
            """)
    TriageResult triage(
            @V("service") String service,
            @V("host") String host,
            @V("level") String level,
            @V("message") String message,
            @V("stackTrace") String stackTrace
    );
}
