package dev.enco.webauth.backend.auth.service;

import dev.enco.webauth.backend.auth.dto.*;
import dev.enco.webauth.backend.auth.exceptions.*;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import dev.enco.webauth.backend.security.service.*;
import dev.enco.webauth.backend.shared.exception.UserNotFoundException;
import dev.enco.webauth.backend.user.entity.User;
import dev.enco.webauth.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BruteForceService bruteForceService;
    private final CurrentUserService currentUserService;

    private final SecurityService securityService;

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UsernameIsPresentException("Аккаунт с таким имененм уже существует");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailIsPresentException("Почта используется другим пользователем");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(securityService.encodePassword(request.password()));
        user.setEmailVerified(false);

        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }

    public GeneratedTokens verifyEmail(VerifyEmailRequest request, String ipAddress) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с указанным адресом почты не найден!"));

        if (!emailService.verifyCode(request.email(), request.code())) {
            bruteForceService.recordVerifyFailure(request.email(), ipAddress);
            throw new InvalidVerificationCodeException("Неверный код верификации");
        }

        bruteForceService.resetVerify(request.email(), ipAddress);

        user.setEmailVerified(true);
        userRepository.save(user);

        return securityService.generateAuthTokens(user.getUsername());
    }

    public GeneratedTokens login(LoginRequest request, String ipAddress) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    bruteForceService.recordLoginFailure(request.username(), ipAddress);
                    return new UserNotFoundException("Пользователь с указанным именем не найден!");
                });

        try {
            securityService.validatePassword(request.password(), user.getPassword());
        } catch (IncorrectPasswordException ex) {
            bruteForceService.recordLoginFailure(request.username(), ipAddress);
            throw ex;
        }

        bruteForceService.resetLogin(request.username(), ipAddress);

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Ваша почта не подтверждена! Проверьте почтовый ящик!");
        }

        return securityService.generateAuthTokens(user.getUsername());
    }

    public GeneratedTokens refresh(String refreshToken) {
        return securityService.refresh(refreshToken);
    }

    public void logout(String accessToken, String refreshToken) {
        securityService.logout(accessToken, refreshToken);
    }

    public void changePassword(String accessToken, ChangePasswordRequest request) {
        User user = currentUserService.getByAccessToken(accessToken);

        securityService.validatePassword(request.currentPassword(), user.getPassword());

        if (request.newPassword() == null || request.newPassword().isBlank()) {
            throw new IllegalArgumentException("Новый пароль не может быть пустым");
        }

        user.setPassword(securityService.encodePassword(request.newPassword()));
        userRepository.save(user);
    }
}
