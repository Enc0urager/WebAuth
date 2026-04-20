package dev.enco.webauth.backend.security.filter;

import dev.enco.webauth.backend.security.properties.RateLimitProperties;
import dev.enco.webauth.backend.security.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitProperties rateLimitProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        Optional<RateLimitProperties.Rule> matchedRule = rateLimitProperties.getRules().stream()
                .filter(rule -> matches(rule, request))
                .findFirst();

        if (matchedRule.isPresent()) {
            RateLimitProperties.Rule rule = matchedRule.get();

            String clientIp = request.getRemoteAddr();
            String key = request.getMethod() + ":" + request.getRequestURI() + ":" + clientIp;

            rateLimitService.checkLimit(
                    key,
                    rule.getLimit(),
                    Duration.ofSeconds(rule.getWindowSeconds())
            );
        }

        filterChain.doFilter(request, response);
    }

    private boolean matches(RateLimitProperties.Rule rule, HttpServletRequest request) {
        boolean methodMatches = rule.getMethods() == null
                || rule.getMethods().isEmpty()
                || rule.getMethods().stream()
                .map(method -> method.toUpperCase(Locale.ROOT))
                .anyMatch(method -> method.equals(request.getMethod()));

        if (!methodMatches) return false;

        return pathMatcher.match(rule.getPath(), request.getRequestURI());
    }
}
