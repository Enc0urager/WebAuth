package dev.enco.webauth.backend.oauth.service;

import dev.enco.webauth.backend.auth.exceptions.EmailIsPresentException;
import dev.enco.webauth.backend.auth.exceptions.UsernameIsPresentException;
import dev.enco.webauth.backend.oauth.dto.PendingOAuthRegister;
import dev.enco.webauth.backend.oauth.entity.OAuthUser;
import dev.enco.webauth.backend.oauth.model.OAuthLoginResult;
import dev.enco.webauth.backend.oauth.model.OAuthProvider;
import dev.enco.webauth.backend.oauth.model.SocialProfile;
import dev.enco.webauth.backend.oauth.repository.OAuthUserRepository;
import dev.enco.webauth.backend.security.model.GeneratedTokens;
import dev.enco.webauth.backend.security.service.SecurityService;
import dev.enco.webauth.backend.shared.exception.UserNotFoundException;
import dev.enco.webauth.backend.user.entity.User;
import dev.enco.webauth.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthUserRepository oauthUserRepository;
    private final UserRepository userRepository;
    private final PendingRegisterService pendingRegisterService;
    private final SecurityService securityService;

    @Transactional
    public OAuthLoginResult login(String registrationId, OAuth2User oauth2User) {
        OAuthProvider provider = OAuthProvider.fromRegistrationId(registrationId);
        SocialProfile profile = provider.extractProfile(oauth2User);

        OAuthUser oauthUser = oauthUserRepository
                .findByProviderAndProviderUserId(provider, profile.providerUserID())
                .orElse(null);

        if (oauthUser != null) {
            GeneratedTokens tokens = securityService.generateAuthTokens(oauthUser.getUser().getUsername());

            return OAuthLoginResult.success(
                    oauthUser.getUser().getUsername(),
                    tokens.accessToken(),
                    tokens.refreshToken()
            );
        }

        String signupToken = pendingRegisterService.create(new PendingOAuthRegister(
                provider,
                profile.providerUserID(),
                profile.email()
        ));

        return OAuthLoginResult.registrationRequired(
                signupToken,
                provider.name().toLowerCase(),
                profile.email()
        );
    }

    @Transactional
    public GeneratedTokens completeRegister(String signupToken, String username) {
        PendingOAuthRegister pending = pendingRegisterService.get(signupToken);

        if (pending == null) {
            throw new UserNotFoundException("Регистрация истекла или не существовала");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameIsPresentException("Аккаунт с таким именем уже существует");
        }

        if (pending.email() != null && userRepository.findByEmail(pending.email()).isPresent()) {
            throw new EmailIsPresentException("Почта используется другим пользователем");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(pending.email() != null ? pending.email() : username);
        user.setPassword(securityService.encodePassword(UUID.randomUUID().toString()));
        user.setEmailVerified(true);

        User saved = userRepository.save(user);

        OAuthUser oauthUser = new OAuthUser();
        oauthUser.setUser(saved);
        oauthUser.setProvider(pending.provider());
        oauthUser.setProviderUserId(pending.providerUserId());
        oauthUser.setEmail(pending.email());

        oauthUserRepository.save(oauthUser);
        pendingRegisterService.delete(signupToken);

        return securityService.generateAuthTokens(user.getUsername());
    }
}
