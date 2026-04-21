package dev.enco.webauth.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Текущий пароль обязателен")
        String currentPassword,

        @NotBlank(message = "Новый пароль обязателен")
        @Size(min = 8, max = 256, message = "Новый пароль должен быть длиной от 8 до 256 символов")
        String newPassword
) {}
