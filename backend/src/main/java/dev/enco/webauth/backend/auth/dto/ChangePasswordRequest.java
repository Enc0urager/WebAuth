package dev.enco.webauth.backend.auth.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {}
