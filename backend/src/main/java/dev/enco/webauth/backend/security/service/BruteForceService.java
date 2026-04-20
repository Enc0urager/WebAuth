package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.properties.BruteForceProperties;
import dev.enco.webauth.backend.security.exception.AntiBruteforceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BruteForceService {

    private final BruteForceProperties bruteForceProperties;
    private final StringRedisTemplate redis;

    public void recordVerifyFailure(String mail, String ip) {
        recordFailure(
                "bf:verify:", mail, ip,
                bruteForceProperties.getVerify().getByEmail(),
                bruteForceProperties.getVerify().getByIp()
        );
    }

    public void recordLoginFailure(String username, String ip) {
        recordFailure(
                "bf:login:", username, ip,
                bruteForceProperties.getLogin().getByUsername(),
                bruteForceProperties.getLogin().getByIp()
        );
    }

    public void resetLogin(String username, String ip) {
        String key = "bf:login:";
        delete(key + username);
        delete(key + ip);
    }

    public void resetVerify(String mail, String ip) {
        String key = "bf:verify:";
        delete(key + mail);
        delete(key + ip);
    }

    public void recordFailure(String redisKey,
                              String key,
                              String ipAddress,
                              BruteForceProperties.Rule keyRule,
                              BruteForceProperties.Rule ipRule
    ) {
        checkAndIncrement(
                redisKey + key,
                keyRule.getMaxAttempts(),
                keyRule.getWindowSeconds()
        );

        checkAndIncrement(
                redisKey + ipAddress,
                ipRule.getMaxAttempts(),
                ipRule.getWindowSeconds()
        );
    }

    private void checkAndIncrement(String redisKey, int maxAttempts, long windowSeconds) {
        Long attempts = redis.opsForValue().increment(redisKey);

        if (attempts != null && attempts == 1) {
            redis.expire(redisKey, Duration.ofSeconds(windowSeconds));
        }

        if (attempts != null && attempts > maxAttempts) {
            throw new AntiBruteforceException("Вы превысили лимит попыток, попробуйте позже!");
        }
    }

    private void delete(String key) {
        redis.delete(key);
    }
}
