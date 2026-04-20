package dev.enco.webauth.backend.minecraft.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.minecraft")
public class MinecraftProperties {
    private String secretHeader;
    private String secretKey;
}
