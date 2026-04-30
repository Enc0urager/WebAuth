package dev.enco.webauth.backend.trustedip.service;

import dev.enco.webauth.backend.security.service.CurrentUserService;
import dev.enco.webauth.backend.trustedip.dto.AddTrustedIpRequest;
import dev.enco.webauth.backend.trustedip.dto.TrustedIpResponse;
import dev.enco.webauth.backend.trustedip.entity.TrustedIp;
import dev.enco.webauth.backend.trustedip.exception.TrustedIpNotFoundException;
import dev.enco.webauth.backend.trustedip.repository.TrustedIpRepository;
import dev.enco.webauth.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrustedIpService {

    private final TrustedIpRepository trustedIpRepository;
    private final CurrentUserService currentUserService;

    public List<TrustedIpResponse> getTrustedIps(String accessToken) {
        User user = currentUserService.getByAccessToken(accessToken);

        return trustedIpRepository.findAllByUsername(user.getUsername()).stream()
                .map(ip -> new TrustedIpResponse(
                        ip.getId(),
                        ip.getIp(),
                        ip.getCreatedAt()
                ))
                .toList();
    }

    public void addTrustedIp(String accessToken, AddTrustedIpRequest request) {
        User user = currentUserService.getByAccessToken(accessToken);

        TrustedIp trustedIp = new TrustedIp();
        trustedIp.setUsername(user.getUsername());
        trustedIp.setIp(request.ipAddress());
        trustedIp.setCreatedAt(Instant.now());

        trustedIpRepository.save(trustedIp);
    }

    public void removeTrustedIp(String accessToken, Long id) {
        User user = currentUserService.getByAccessToken(accessToken);

        TrustedIp trustedIp = trustedIpRepository.findById(id)
                .orElseThrow(() -> new TrustedIpNotFoundException("Доверенный IP не найден"));

        if (!trustedIp.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("Этот IP не принадлежит текущему пользователю");
        }

        trustedIpRepository.delete(trustedIp);
    }
}
