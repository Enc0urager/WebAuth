package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.exception.RateLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final StringRedisTemplate redis;

    public void checkLimit(String key, int limit, Duration window) {
        String redisKey = "rl:" + key;

        Long count = redis.opsForValue().increment(redisKey);

        if (count != null && count == 1) {
            redis.expire(redisKey, window);
        }

        if (count != null && count > limit) {
            throw new RateLimitException("Вы превысили лимит запросов, попробуйте позже!");
        }
    }
}
