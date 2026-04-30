package dev.enco.webauth.backend.oauth.model;

public record OAuthLoginResult(
        OAuthLoginStatus status,
        String username,
        String signupToken,
        String provider,
        String email,
        String accessToken,
        String refreshToken
) {
    public static OAuthLoginResult success(String username,
                                           String accessToken,
                                           String refreshToken) {
        return new OAuthLoginResult(
                OAuthLoginStatus.SUCCESS,
                username,
                null,
                null,
                null,
                accessToken,
                refreshToken
        );
    }

    public static OAuthLoginResult registrationRequired(String signupToken,
                                                        String provider,
                                                        String email) {
        return new OAuthLoginResult(
                OAuthLoginStatus.NICKNAME_REQUIRED,
                null,
                signupToken,
                provider,
                email,
                null,
                null
        );
    }
}
