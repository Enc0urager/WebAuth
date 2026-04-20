package dev.enco.webauth.backend.minecraft.dto;

public record JoinRequestEvent(
        Long requestId,
        String username,
        String status
) {}
