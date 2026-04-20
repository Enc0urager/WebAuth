package dev.enco.webauth.backend.user.dto;

import java.time.Instant;

public record ProfileResponse(
        String username,
        String email,
        Instant lastLogin,
        Instant lastJoin
) {}
