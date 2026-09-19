package com.logtriage.engine.producer;

import com.logtriage.engine.model.DlqRecord;
import com.logtriage.engine.model.LogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.dlq}")
    private String dlqTopic;

    public void send(LogEvent event, Throwable error) {
        DlqRecord record = new DlqRecord(
                event,
                error.getClass().getName(),
                error.getMessage(),
                Instant.now()
        );

        kafkaTemplate.send(dlqTopic, event.eventId(), record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish DLQ record for event {}", event.eventId(), ex);
                    }
                });
    }
}
