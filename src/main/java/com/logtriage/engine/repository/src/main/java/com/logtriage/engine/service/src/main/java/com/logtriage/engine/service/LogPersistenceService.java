package com.logtriage.engine.service;

import com.logtriage.engine.entity.LogEventEntity;
import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.model.TriageResult;
import com.logtriage.engine.repository.LogEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogPersistenceService {

    private final LogEventRepository repository;

    @Transactional
    public void saveRaw(LogEvent event) {
        repository.save(toEntity(event, null));
    }

    @Transactional
    public void saveWithTriage(LogEvent event, TriageResult result) {
        repository.save(toEntity(event, result));
    }

    private LogEventEntity toEntity(LogEvent event, TriageResult result) {
        LogEventEntity.LogEventEntityBuilder builder = LogEventEntity.builder()
                .eventId(event.eventId())
                .service(event.service())
                .host(event.host())
                .level(event.level())
                .message(event.message())
                .stackTrace(event.stackTrace())
                .timestamp(event.timestamp())
                .createdAt(Instant.now());

        if (result != null) {
            builder.severity(result.severity())
                    .rootCauseCategory(result.rootCauseCategory())
                    .triageSummary(result.summary())
                    .suggestedAction(result.suggestedAction())
                    .recurringPattern(result.recurringPattern())
                    .confidence(result.confidence());
        }

        return builder.build();
    }
}
