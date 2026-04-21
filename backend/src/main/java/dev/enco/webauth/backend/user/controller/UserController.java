package dev.enco.webauth.backend.user.controller;

import dev.enco.webauth.backend.auth.dto.ChangePasswordRequest;
import dev.enco.webauth.backend.user.dto.ProfileResponse;
import dev.enco.webauth.backend.auth.service.AuthService;
import dev.enco.webauth.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    public ProfileResponse me(@RequestHeader("Authorization") String header) {
        return userService.getProfile(extractToken(header));
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestHeader("Authorization") String header,
                               @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(extractToken(header), request);
    }

    private String extractToken(String header) {
        return header.substring(7).trim();
    }
}
