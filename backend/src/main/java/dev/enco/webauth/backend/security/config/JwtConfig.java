package dev.enco.webauth.backend.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.enco.webauth.backend.security.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtProperties keys;

    @Bean
    public JwtEncoder jwtEncoder() {
        RSAKey rsaKey = new RSAKey.Builder(keys.getPublicKey())
                .privateKey(keys.getPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        JWKSource<SecurityContext> jwkSource =
                (jwkSelector, context) -> jwkSelector.select(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withPublicKey(keys.getPublicKey())
                .build();
    }
}
