package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.refresh-cookie")
public class RefreshCookieProperties {
    private String name;
    private boolean secure;
    private String sameSite;
    private String path;
    private long ttlSeconds;
}
