package dev.enco.webauth.backend.oauth.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public enum OAuthProvider {
    GOOGLE((provider, user) -> new SocialProfile(
            provider,
            getStringAttribute(user, "sub"),
            getStringAttribute(user, "email")
    )),

    DISCORD((provider, user) -> new SocialProfile(
            provider,
            getStringAttribute(user, "id"),
            getStringAttribute(user, "email")
    ));

    private final ProfileExtractor extractor;

    public SocialProfile extractProfile(OAuth2User oauth2User) {
        return extractor.extract(this, oauth2User);
    }

    public static OAuthProvider fromRegistrationId(String registrationId) {
        if (registrationId == null) {
            throw new IllegalArgumentException("OAuth registrationId is null");
        }

        return switch (registrationId.toLowerCase()) {
            case "google" -> GOOGLE;
            case "discord" -> DISCORD;
            default -> throw new IllegalArgumentException("Unknown OAuth provider: " + registrationId);
        };
    }

    private static String getStringAttribute(OAuth2User user, String name) {
        Object value = user.getAttributes().get(name);
        return value == null ? null : String.valueOf(value);
    }

    @FunctionalInterface
    interface ProfileExtractor {
        SocialProfile extract(OAuthProvider provider, OAuth2User user);
    }
}
