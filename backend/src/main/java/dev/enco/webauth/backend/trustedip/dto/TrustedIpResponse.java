package dev.enco.webauth.backend.trustedip.dto;

import java.time.Instant;

public record TrustedIpResponse(
    Long id,
    String ip,
    Instant createdAt
){}
