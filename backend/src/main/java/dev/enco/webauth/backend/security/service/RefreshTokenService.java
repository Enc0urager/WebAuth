package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.properties.RefreshCookieProperties;
import dev.enco.webauth.backend.security.exception.UsedTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshCookieProperties properties;
    private final StringRedisTemplate redis;

    public void store(String username, String jti) {
        redis.opsForValue().set(
                "rt:" + jti,
                username,
                Duration.ofSeconds(properties.getTtlSeconds())
        );
    }

    public String validate(String jti) {
        if (redis.hasKey("rt_used:" + jti)) {
            throw new UsedTokenException("Ваш токен авторизации уже использован");
        }
        return redis.opsForValue().get("rt:" + jti);
    }

    public void markUsed(String jti) {
        redis.opsForValue().set(
                "rt_used:" + jti,
                "1",
                Duration.ofSeconds(properties.getTtlSeconds())
        );
    }

    public void delete(String jti) {
        redis.delete("rt:" + jti);
    }
}
