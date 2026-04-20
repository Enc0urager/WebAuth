package dev.enco.webauth.backend.minecraft.dto;

import dev.enco.webauth.backend.minecraft.enums.JoinRequestStatus;

import java.time.Instant;

public record JoinRequestResponse(
        Long id,
        String ip,
        String country,
        String city,
        Instant requestedAt,
        JoinRequestStatus status
) {}
