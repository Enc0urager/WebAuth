package dev.enco.webauth.backend.security.controller;

import dev.enco.webauth.backend.auth.dto.AuthResponse;
import dev.enco.webauth.backend.auth.service.AuthService;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import dev.enco.webauth.backend.security.service.RefreshCookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final AuthService authService;
    private final RefreshCookieService refreshCookieService;

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String header,
                       HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse) {
        String accessToken = header.substring(7).trim();
        String refreshToken = refreshCookieService.extractCookie(httpRequest);

        authService.logout(accessToken, refreshToken);
        refreshCookieService.clearRefreshCookie(httpResponse);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(HttpServletRequest httpRequest,
                                HttpServletResponse httpResponse) {
        String refreshToken = refreshCookieService.extractCookie(httpRequest);

        GeneratedTokens tokens = authService.refresh(refreshToken);
        refreshCookieService.addRefreshCookie(httpResponse, tokens.refreshToken());

        return new AuthResponse(tokens.accessToken());
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) {
        return Map.of(
                "headerName", csrfToken.getHeaderName(),
                "parameterName", csrfToken.getParameterName(),
                "token", csrfToken.getToken()
        );
    }
}
