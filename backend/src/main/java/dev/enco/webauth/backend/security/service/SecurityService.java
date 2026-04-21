package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.auth.dto.AuthResponse;
import dev.enco.webauth.backend.auth.dto.RefreshRequest;
import dev.enco.webauth.backend.auth.exceptions.IncorrectPasswordException;
import dev.enco.webauth.backend.security.exception.InvalidRefreshTokenException;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void validatePassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new IncorrectPasswordException("Неверный пароль");
        }
    }

    public GeneratedTokens generateAuthTokens(String username) {
        String access = jwtService.generateAccessToken(username);
        String refresh = jwtService.generateRefreshToken(username);
        refreshTokenService.store(username, refresh);
        return new GeneratedTokens(access, refresh);
    }

    public GeneratedTokens refresh(RefreshRequest request) {
        String token = request.refreshToken();
        String username = refreshTokenService.validate(token);

        if (username == null) {
            throw new InvalidRefreshTokenException("Недопустимый токен обновления");
        }

        refreshTokenService.markUsed(token);
        refreshTokenService.delete(token);

        return generateAuthTokens(username);
    }

    public void logout(String accessToken, String refreshToken) {
        Jwt jwt = jwtService.decode(accessToken);

        Duration ttl = Duration.between(Instant.now(), jwt.getExpiresAt());
        tokenBlacklistService.blacklist(jwt.getId(), ttl);
        refreshTokenService.delete(refreshToken);
    }
}
