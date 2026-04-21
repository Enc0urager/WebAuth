package dev.enco.webauth.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(
        @NotBlank(message = "Электронная почта обязательна")
        @Email(message = "Некорректный адрес электронной почты")
        @Size(max = 254, message = "Электронная почта слишком длинная")
        String email,

        @NotBlank(message = "Код подтверждения обязателен")
        @Pattern(regexp = "^\\d{6}$", message = "Код подтверждения должен состоять из 6 цифр")
        String code
) {}
