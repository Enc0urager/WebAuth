package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.rate-limit")
public class RateLimitProperties {

    private List<Rule> rules = new ArrayList<>();

    @Getter
    @Setter
    public static class Rule {
        private String path;
        private List<String> methods = new ArrayList<>();
        private int limit;
        private long windowSeconds;
    }
}
