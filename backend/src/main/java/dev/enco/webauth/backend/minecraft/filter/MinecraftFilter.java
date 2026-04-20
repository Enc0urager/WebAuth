package dev.enco.webauth.backend.minecraft.filter;

import dev.enco.webauth.backend.minecraft.properties.MinecraftProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MinecraftFilter extends OncePerRequestFilter {
    private final MinecraftProperties properties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/internal/minecraft");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String expectedSecret = properties.getSecretKey();
        String headerName = properties.getSecretHeader();
        String providedSecret = request.getHeader(headerName);

        if (expectedSecret == null || expectedSecret.isBlank()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Minecraft secret is not configured");
            return;
        }

        if (providedSecret == null || !providedSecret.equals(expectedSecret)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid minecraft secret");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
