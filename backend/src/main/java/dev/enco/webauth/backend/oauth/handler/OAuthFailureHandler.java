package dev.enco.webauth.backend.oauth.handler;

import dev.enco.webauth.backend.oauth.properties.OAuthProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthFailureHandler implements AuthenticationFailureHandler {

    private final OAuthProperties properties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String redirectUrl = UriComponentsBuilder
                .fromUriString(properties.getErrorUrl())
                .queryParam("error", "Авторизация через внешний сервис не удалась")
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
