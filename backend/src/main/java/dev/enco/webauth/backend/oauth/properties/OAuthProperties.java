package dev.enco.webauth.backend.oauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.oauth2")
public class OAuthProperties {
    private String successUrl;
    private String signupUrl;
    private String errorUrl;
    private long pendingSignupTtlSeconds;
}
