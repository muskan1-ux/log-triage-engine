package com.logtriage.engine.consumer;

import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.service.LogProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogEventConsumer {

    private final LogProcessingService processingService;

    @KafkaListener(
            topics = "${app.kafka.topics.raw-logs}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, LogEvent> record) {
        LogEvent event = record.value();
        if (event == null) {
            log.warn("Null LogEvent at partition={} offset={}, skipping", record.partition(), record.offset());
            return;
        }
        processingService.handle(event);
    }
}
