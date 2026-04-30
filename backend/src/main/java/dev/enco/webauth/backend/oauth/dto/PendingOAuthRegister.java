package dev.enco.webauth.backend.oauth.dto;

import dev.enco.webauth.backend.oauth.model.OAuthProvider;

public record PendingOAuthRegister(
        OAuthProvider provider,
        String providerUserId,
        String email
) {}
