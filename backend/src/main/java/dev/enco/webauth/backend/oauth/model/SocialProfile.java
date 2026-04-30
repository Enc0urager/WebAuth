package dev.enco.webauth.backend.oauth.model;

public record SocialProfile(
        OAuthProvider provider,
        String providerUserID,
        String email
) {}
