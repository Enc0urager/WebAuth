package dev.enco.webauth.backend.auth.service;

import dev.enco.webauth.backend.auth.properties.EmailProperties;
import dev.enco.webauth.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailProperties properties;
    private final StringRedisTemplate redis;
    private final JavaMailSender mailSender;
    private final SecureRandom random = new SecureRandom();

    @Async
    public void sendVerificationEmail(User user) {
        String code = generateCode();
        System.out.println(code); //TODO:УБРАТЬ

        redis.opsForValue().set(
                "email:" + user.getEmail(),
                code,
                Duration.ofMinutes(properties.getTtlSeconds())
        );

        mailSender.send(mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(properties.getFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(properties.getSubject());
            helper.setText(properties.getText().formatted(code), false);
        });
    }

    public boolean verifyCode(String email, String code) {
        String stored = redis.opsForValue().get("email:" + email);
        if (stored == null) return false;

        return code.equals(stored);
    }

    private String generateCode() {
        return String.valueOf(random.nextInt(100000, 999999));
    }
}
