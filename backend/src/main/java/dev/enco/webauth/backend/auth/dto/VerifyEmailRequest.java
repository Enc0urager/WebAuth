package dev.enco.webauth.backend.auth.dto;

public record VerifyEmailRequest(
        String email,
        String code
) {}
