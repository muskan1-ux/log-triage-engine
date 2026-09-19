package com.logtriage.engine.producer;

import com.logtriage.engine.model.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class LogEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.raw-logs}")
    private String rawLogsTopic;

    public CompletableFuture<SendResult<String, Object>> publish(LogEvent event) {
        return kafkaTemplate.send(rawLogsTopic, event.service(), event);
    }
}
