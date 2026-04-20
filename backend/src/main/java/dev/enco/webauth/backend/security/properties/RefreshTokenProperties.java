package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.refresh-token")
public class RefreshTokenProperties {
    private long ttlSeconds;
    private long usedTtlSeconds;
}
