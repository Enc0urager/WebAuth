package dev.enco.webauth.backend.auth.controller;

import dev.enco.webauth.backend.auth.dto.*;
import dev.enco.webauth.backend.auth.service.AuthService;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import dev.enco.webauth.backend.security.service.RefreshCookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshCookieService refreshCookieService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request,
                              HttpServletRequest httpRequest,
                              HttpServletResponse httpResponse
    ) {
        GeneratedTokens tokens = authService.login(request, httpRequest.getRemoteAddr());
        refreshCookieService.addRefreshCookie(httpResponse, tokens.refreshToken());
        return new AuthResponse(tokens.accessToken());
    }

    @PostMapping("/verify")
    public AuthResponse verify(@Valid @RequestBody VerifyEmailRequest request,
                               HttpServletRequest httpRequest,
                               HttpServletResponse httpResponse
    ) {
        GeneratedTokens tokens = authService.verifyEmail(request, httpRequest.getRemoteAddr());
        refreshCookieService.addRefreshCookie(httpResponse, tokens.refreshToken());
        return new AuthResponse(tokens.accessToken());
    }
}
