package dev.enco.webauth.backend.oauth.controller;

import dev.enco.webauth.backend.auth.dto.AuthResponse;
import dev.enco.webauth.backend.oauth.dto.CompleteOAuthRegisterRequest;
import dev.enco.webauth.backend.oauth.service.OAuthService;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import dev.enco.webauth.backend.security.service.RefreshCookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;
    private final RefreshCookieService refreshCookieService;

    @PostMapping("/complete-register")
    public AuthResponse completeRegister(@Valid @RequestBody CompleteOAuthRegisterRequest request,
                                         HttpServletResponse response) {
        GeneratedTokens tokens = oauthService.completeRegister(
                request.signupToken(),
                request.username()
        );

        refreshCookieService.addRefreshCookie(response, tokens.refreshToken());

        return new AuthResponse(tokens.accessToken());
    }
}
