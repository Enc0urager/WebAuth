package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String issuer;
    private long accessExpiration;
    private long refreshExpiration;
}