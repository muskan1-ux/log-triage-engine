package com.logtriage.engine.service;

import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.model.TriageResult;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogTriageOrchestrator {

    private static final String INSTANCE = "logTriage";

    private final LogTriageService logTriageService;

    @Bulkhead(name = INSTANCE)
    @CircuitBreaker(name = INSTANCE, fallbackMethod = "triageFallback")
    @Retry(name = INSTANCE)
    @TimeLimiter(name = INSTANCE)
    public CompletableFuture<TriageResult> triageAsync(LogEvent event) {
        return CompletableFuture.supplyAsync(() -> logTriageService.triage(
                event.service(),
                event.host(),
                event.level().name(),
                event.message(),
                event.stackTrace() == null ? "" : event.stackTrace()
        ));
    }

    @SuppressWarnings("unused")
    private CompletableFuture<TriageResult> triageFallback(LogEvent event, Throwable throwable) {
        log.warn("LLM triage unavailable for event {}: {}", event.eventId(), throwable.toString());
        return CompletableFuture.completedFuture(TriageResult.fallback(throwable.getClass().getSimpleName()));
    }
}
