package dev.enco.webauth.backend.shared.dto;

import java.time.Instant;

public record ErrorResponse(
        String message,
        int status,
        String path,
        Instant timestamp
) {}
