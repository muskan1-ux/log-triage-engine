package com.logtriage.engine.controller;

import com.logtriage.engine.model.LogEvent;
import com.logtriage.engine.producer.LogEventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LogIngestController {

    private final LogEventProducer producer;

    @PostMapping("/api/logs")
    public ResponseEntity<Map<String, String>> publish(@Valid @RequestBody LogEvent event) {
        producer.publish(event);
        return ResponseEntity.accepted().body(Map.of("eventId", event.eventId(), "status", "queued"));
    }
}
