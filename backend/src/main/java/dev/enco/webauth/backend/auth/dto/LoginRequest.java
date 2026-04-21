package dev.enco.webauth.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 4, max = 16, message = "Имя пользователя должно иметь длинну от 4 до 16 символов")
        String username,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 256, message = "Пароль должен быть длиной от 8 до 256 символов")
        String password
) {}