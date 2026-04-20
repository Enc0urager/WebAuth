package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.properties.RefreshTokenProperties;
import dev.enco.webauth.backend.security.exception.UsedTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenProperties properties;
    private final StringRedisTemplate redis;

    public void store(String username, String token) {
        redis.opsForValue().set(
                "rt:" + token,
                username,
                Duration.ofSeconds(properties.getTtlSeconds())
        );
    }

    public String validate(String token) {
        if (redis.hasKey("rt_used:" + token)) {
            throw new UsedTokenException("Ваш токен авторизации уже использован");
        }
        return redis.opsForValue().get("rt:" + token);
    }

    public void markUsed(String token) {
        redis.opsForValue().set(
                "rt_used:" + token,
                "1",
                Duration.ofSeconds(properties.getUsedTtlSeconds())
        );
    }

    public void delete(String token) {
        redis.delete("rt:" + token);
    }
}
