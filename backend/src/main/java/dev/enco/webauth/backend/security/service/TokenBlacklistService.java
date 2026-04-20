package dev.enco.webauth.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redis;

    public void blacklist(String jti, Duration ttl) {
        redis.opsForValue().set(
                "bl:" + jti,
                "1",
                ttl
        );
    }

    public boolean isBlacklisted(String jti) {
        return redis.hasKey("bl:" + jti);
    }
}
