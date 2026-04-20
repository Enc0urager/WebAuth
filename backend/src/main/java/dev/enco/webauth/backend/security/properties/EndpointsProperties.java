package dev.enco.webauth.backend.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.endpoints")
public class EndpointsProperties {
    private List<String> publicEndpoints = new ArrayList<>();
}
