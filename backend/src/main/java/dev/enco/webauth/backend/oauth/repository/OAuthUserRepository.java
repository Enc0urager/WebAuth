package dev.enco.webauth.backend.oauth.repository;

import dev.enco.webauth.backend.oauth.entity.OAuthUser;
import dev.enco.webauth.backend.oauth.model.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, String> {
    Optional<OAuthUser> findByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}
