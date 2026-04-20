package dev.enco.webauth.backend.auth.controller;

import dev.enco.webauth.backend.auth.dto.*;
import dev.enco.webauth.backend.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request,
                              HttpServletRequest httpRequest
    ) {
        return authService.login(request, httpRequest.getRemoteAddr());
    }

    @PostMapping("/verify")
    public AuthResponse verify(@RequestBody VerifyEmailRequest request,
                               HttpServletRequest httpRequest
    ) {
        return authService.verifyEmail(request, httpRequest.getRemoteAddr());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

}
