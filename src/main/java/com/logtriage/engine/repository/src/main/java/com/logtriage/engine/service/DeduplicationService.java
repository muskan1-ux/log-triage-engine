package com.logtriage.engine.service;

import com.logtriage.engine.model.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DeduplicationService {

    private static final Duration DEDUP_TTL = Duration.ofMinutes(10);
    private static final String KEY_PREFIX = "dedup:log:";

    private final StringRedisTemplate redisTemplate;

    public boolean isDuplicate(LogEvent event) {
        String key = KEY_PREFIX + event.eventId();
        Boolean firstSeen = redisTemplate.opsForValue().setIfAbsent(key, "1", DEDUP_TTL);
        return firstSeen == null || !firstSeen;
    }
}
