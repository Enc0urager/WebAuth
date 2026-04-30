package dev.enco.webauth.backend.oauth.handler;

import dev.enco.webauth.backend.oauth.model.OAuthLoginResult;
import dev.enco.webauth.backend.oauth.model.OAuthLoginStatus;
import dev.enco.webauth.backend.oauth.properties.OAuthProperties;
import dev.enco.webauth.backend.oauth.service.OAuthService;
import dev.enco.webauth.backend.security.service.RefreshCookieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oauthService;
    private final RefreshCookieService refreshCookieService;
    private final OAuthProperties properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuthLoginResult result = oauthService.login(
                token.getAuthorizedClientRegistrationId(),
                token.getPrincipal()
        );

        if (result.status() == OAuthLoginStatus.SUCCESS) {
            refreshCookieService.addRefreshCookie(response, result.refreshToken());

            String redirectUrl = UriComponentsBuilder
                    .fromUriString(properties.getSuccessUrl())
                    .queryParam("accessToken", result.accessToken())
                    .build()
                    .toUriString();

            response.sendRedirect(redirectUrl);
            return;
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(properties.getSignupUrl())
                .queryParam("signupToken", result.signupToken())
                .queryParam("provider", result.provider())
                .queryParam("email", result.email())
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
