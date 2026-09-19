package com.logtriage.engine.service;

import com.logtriage.engine.exception.RetryableProcessingException;
import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.producer.DlqProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogProcessingService {

    private final DeduplicationService deduplicationService;
    private final LogTriageOrchestrator triageOrchestrator;
    private final LogPersistenceService persistenceService;
    private final DlqProducer dlqProducer;

    @Qualifier("triageExecutor")
    private final Executor triageExecutor;

    public void handle(LogEvent event) {
        if (deduplicationService.isDuplicate(event)) {
            log.debug("Duplicate event {} skipped", event.eventId());
            return;
        }

        if (event.level().requiresTriage()) {
            dispatchForTriage(event);
        } else {
            persistenceService.saveRaw(event);
        }
    }

    private void dispatchForTriage(LogEvent event) {
        try {
            triageExecutor.execute(() -> runTriage(event));
        } catch (RejectedExecutionException rex) {
            throw new RetryableProcessingException(
                    "Triage executor saturated for event " + event.eventId(), rex);
        }
    }

    private void runTriage(LogEvent event) {
        triageOrchestrator.triageAsync(event)
                .thenAccept(result -> persistenceService.saveWithTriage(event, result))
                .exceptionally(ex -> {
                    log.error("Unrecoverable triage failure for event {}", event.eventId(), ex);
                    dlqProducer.send(event, ex);
                    return null;
                });
    }
}
