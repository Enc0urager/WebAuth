package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.brute-force")
public class BruteForceProperties {

    private Login login = new Login();
    private Verify verify = new Verify();

    @Getter
    @Setter
    public static class Login {
        private Rule byUsername = new Rule();
        private Rule byIp = new Rule();
    }

    @Getter
    @Setter
    public static class Verify {
        private Rule byEmail = new Rule();
        private Rule byIp = new Rule();
    }

    @Getter
    @Setter
    public static class Rule {
        private int maxAttempts;
        private long windowSeconds;
    }
}
