package dev.enco.webauth.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 4, max = 16, message = "Имя пользователя должно быть длиной от 3 до 32 символов")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Недопустимое имя пользователя"
        )
        String username,

        @NotBlank(message = "Электронная почта обязательна")
        @Email(message = "Некорректный адрес электронной почты")
        @Size(max = 254, message = "Электронная почта слишком длинная")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 256, message = "Пароль должен быть длиной от 8 до 256 символов")
        String password
) {}
