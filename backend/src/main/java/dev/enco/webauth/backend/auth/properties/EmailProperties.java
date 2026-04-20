package dev.enco.webauth.backend.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {
    private String from;
    private String subject;
    private String text;
    private long ttlSeconds;
}
