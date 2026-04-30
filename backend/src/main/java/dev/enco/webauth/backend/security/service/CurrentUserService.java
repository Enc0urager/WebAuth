package dev.enco.webauth.backend.security.service;

import dev.enco.webauth.backend.shared.exception.UserNotFoundException;
import dev.enco.webauth.backend.user.entity.User;
import dev.enco.webauth.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User getByAccessToken(String accessToken) {
        Jwt jwt = jwtService.decode(accessToken);
        String username = jwt.getSubject();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }
}
