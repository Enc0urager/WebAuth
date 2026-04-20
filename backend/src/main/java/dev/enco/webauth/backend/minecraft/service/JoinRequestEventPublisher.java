package dev.enco.webauth.backend.minecraft.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enco.webauth.backend.minecraft.dto.JoinRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinRequestEventPublisher {

    private static final String CHANNEL = "minecraft:join-request-events";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publish(Long requestId, String username, String status) {
        try {
            String payload = objectMapper.writeValueAsString(
                    new JoinRequestEvent(requestId, username, status)
            );
            redisTemplate.convertAndSend(CHANNEL, payload);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отправить событие join request", e);
        }
    }
}
