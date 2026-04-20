package dev.enco.webauth.backend.security.filter;

import dev.enco.webauth.backend.security.exception.BlacklistedTokenException;
import dev.enco.webauth.backend.security.exception.InvalidTokenTypeException;
import dev.enco.webauth.backend.security.service.JwtService;
import dev.enco.webauth.backend.security.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Jwt jwt = jwtService.decode(token);

            if (!jwt.getClaim("type").equals("access")) {
                throw new InvalidTokenTypeException("Недопустимый тип токена авторизации");
            }

            String jti = jwt.getId();
            if (blacklistService.isBlacklisted(jti)) {
                throw new BlacklistedTokenException("Ваш токен авторизации был отозван");
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    jwt.getSubject(),
                    null,
                    List.of()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}