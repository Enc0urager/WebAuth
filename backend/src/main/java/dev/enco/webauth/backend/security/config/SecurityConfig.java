package dev.enco.webauth.backend.security.config;

import dev.enco.webauth.backend.minecraft.filter.MinecraftFilter;
import dev.enco.webauth.backend.oauth.handler.OAuthFailureHandler;
import dev.enco.webauth.backend.oauth.handler.OAuthSuccessHandler;
import dev.enco.webauth.backend.security.filter.JwtAuthFilter;
import dev.enco.webauth.backend.security.filter.RateLimitFilter;
import dev.enco.webauth.backend.security.properties.EndpointsProperties;
import dev.enco.webauth.backend.security.properties.RefreshCookieProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;
    private final MinecraftFilter minecraftFilter;
    private final EndpointsProperties endpointsProperties;
    private final RefreshCookieProperties refreshCookieProperties;
    private final OAuthSuccessHandler oauthSuccessHandler;
    private final OAuthFailureHandler oauthFailureHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] publicEndpoints = endpointsProperties.getPublicEndpoints().toArray(String[]::new);

        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath(refreshCookieProperties.getPath());
        csrfTokenRepository.setHeaderName(refreshCookieProperties.getHeaderName());

        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .ignoringRequestMatchers(
                                endpointsProperties.getCsrfDisabled().toArray(String[]::new)
                        )
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(oauthSuccessHandler)
                        .failureHandler(oauthFailureHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoints).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(minecraftFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
