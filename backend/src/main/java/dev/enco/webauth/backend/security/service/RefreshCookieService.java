package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.security.properties.RefreshCookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshCookieService {

    private final RefreshCookieProperties properties;

    public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(properties.getName(), refreshToken)
                .httpOnly(true)
                .secure(properties.isSecure())
                .sameSite(properties.getSameSite())
                .path(properties.getPath())
                .maxAge(Duration.ofSeconds(properties.getTtlSeconds()))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(properties.getName(), "")
                .httpOnly(true)
                .secure(properties.isSecure())
                .sameSite(properties.getSameSite())
                .path(properties.getPath())
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String extractCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (properties.getName().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
