package dev.enco.webauth.backend.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enco.webauth.backend.oauth.dto.PendingOAuthRegister;
import dev.enco.webauth.backend.oauth.properties.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PendingRegisterService {

    private static final String PREFIX = "oauth:pr:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OAuthProperties properties;

    public String create(PendingOAuthRegister register) {
        try {
            String token = UUID.randomUUID().toString();
            String value = objectMapper.writeValueAsString(register);

            redisTemplate.opsForValue().set(
                    PREFIX + token,
                    value,
                    Duration.ofSeconds(properties.getPendingSignupTtlSeconds())
            );

            return token;
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка создания регистрации", e);
        }
    }

    public PendingOAuthRegister get(String token) {
        try {
            String value = redisTemplate.opsForValue().get(PREFIX + token);

            if (value == null) return null;

            return objectMapper.readValue(value, PendingOAuthRegister.class);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка чтения регистрации", e);
        }
    }

    public void delete(String token) {
        redisTemplate.delete(PREFIX + token);
    }
}
