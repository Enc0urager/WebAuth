package dev.enco.webauth.backend.security.config;

import dev.enco.webauth.backend.minecraft.filter.MinecraftFilter;
import dev.enco.webauth.backend.security.filter.JwtAuthFilter;
import dev.enco.webauth.backend.security.filter.RateLimitFilter;
import dev.enco.webauth.backend.security.properties.EndpointsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;
    private final MinecraftFilter minecraftFilter;
    private final EndpointsProperties endpointsProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] publicEndpoints = endpointsProperties.getPublicEndpoints().toArray(String[]::new);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
