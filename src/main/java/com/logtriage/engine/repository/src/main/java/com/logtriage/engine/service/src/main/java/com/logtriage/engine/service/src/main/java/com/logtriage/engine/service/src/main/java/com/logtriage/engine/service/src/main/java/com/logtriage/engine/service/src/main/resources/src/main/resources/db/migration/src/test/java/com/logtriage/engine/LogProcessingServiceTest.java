package com.logtriage.engine;

import com.logtriage.engine.exception.RetryableProcessingException;
import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.model.LogLevel;
import com.logtriage.engine.producer.DlqProducer;
import com.logtriage.engine.service.DeduplicationService;
import com.logtriage.engine.service.LogPersistenceService;
import com.logtriage.engine.service.LogProcessingService;
import com.logtriage.engine.service.LogTriageOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogProcessingServiceTest {

    @Mock private DeduplicationService deduplicationService;
    @Mock private LogTriageOrchestrator triageOrchestrator;
    @Mock private LogPersistenceService persistenceService;
    @Mock private DlqProducer dlqProducer;

    private LogProcessingService processingService;

    @BeforeEach
    void setUp() {
        Executor synchronousExecutor = Runnable::run;
        processingService = new LogProcessingService(
                deduplicationService, triageOrchestrator, persistenceService, dlqProducer, synchronousExecutor);
    }

    @Test
    void duplicateEventIsSkippedEntirely() {
        LogEvent event = sampleEvent(LogLevel.ERROR);
        when(deduplicationService.isDuplicate(event)).thenReturn(true);

        processingService.handle(event);

        verify(persistenceService, never()).saveRaw(event);
        verify(triageOrchestrator, never()).triageAsync(event);
    }

    @Test
    void infoLevelEventIsPersistedDirectlyWithoutTriage() {
        LogEvent event = sampleEvent(LogLevel.INFO);
        when(deduplicationService.isDuplicate(event)).thenReturn(false);

        processingService.handle(event);

        verify(persistenceService).saveRaw(event);
        verify(triageOrchestrator, never()).triageAsync(event);
    }

    @Test
    void executorSaturationSurfacesAsRetryableException() {
        LogEvent event = sampleEvent(LogLevel.ERROR);
        Executor rejectingExecutor = task -> { throw new RejectedExecutionException("pool full"); };
        LogProcessingService service = new LogProcessingService(
                deduplicationService, triageOrchestrator, persistenceService, dlqProducer, rejectingExecutor);
        when(deduplicationService.isDuplicate(event)).thenReturn(false);

        assertThatThrownBy(() -> service.handle(event))
                .isInstanceOf(RetryableProcessingException.class);
    }

    private LogEvent sampleEvent(LogLevel level) {
        return new LogEvent("evt-1", "checkout-service", "host-1", level, "boom", null, Instant.now());
    }
}
