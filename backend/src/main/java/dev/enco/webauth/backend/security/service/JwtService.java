package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;
    private final JwtDecoder decoder;
    private final JwtEncoder encoder;

    public Jwt decode(String token) {
        return decoder.decode(token);
    }

    public String generateAccessToken(String username) {
        return generateToken(
                username,
                Duration.ofMillis(properties.getAccessExpiration()),
                "access"
        );
    }

    public String generateRefreshToken(String username) {
        return generateToken(
                username,
                Duration.ofMillis(properties.getRefreshExpiration()),
                "refresh"
        );
    }

    private String generateToken(String username, Duration ttl, String type) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .claim("type", type)
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .issuer(properties.getIssuer())
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}