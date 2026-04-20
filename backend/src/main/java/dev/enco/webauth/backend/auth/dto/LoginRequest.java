package dev.enco.webauth.backend.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}