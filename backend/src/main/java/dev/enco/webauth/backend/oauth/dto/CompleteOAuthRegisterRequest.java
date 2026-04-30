package dev.enco.webauth.backend.oauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompleteOAuthRegisterRequest(
        @NotBlank(message = "Токен завершения регистрации обязателен")
        String signupToken,

        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 4, max = 16, message = "Имя пользователя должно иметь длинну от 4 до 16 символов")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Недопустимое имя пользователя"
        )
        String username
) {}
