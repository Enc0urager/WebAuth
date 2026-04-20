package dev.enco.webauth.backend.user.service;

import dev.enco.webauth.backend.security.service.CurrentUserService;
import dev.enco.webauth.backend.user.dto.ProfileResponse;
import dev.enco.webauth.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CurrentUserService currentUserService;

    public ProfileResponse getProfile(String accessToken) {
        User user = currentUserService.getByAccessToken(accessToken);

        return new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getLastLogin(),
                user.getLastJoin()
        );
    }
}
