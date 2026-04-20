package dev.enco.webauth.backend.auth.dto;

import jakarta.validation.constraints.Email;

public record RegisterRequest(
        String username,
        @Email(message = "Некорректный адресс электронной почты")
        String email,
        String password
) {}
