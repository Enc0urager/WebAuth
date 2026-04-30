package dev.enco.webauth.backend.minecraft.service;

import dev.enco.webauth.backend.minecraft.dto.CreateJoinRequest;
import dev.enco.webauth.backend.minecraft.dto.CreateJoinResponse;
import dev.enco.webauth.backend.minecraft.dto.JoinRequestResponse;
import dev.enco.webauth.backend.minecraft.entity.JoinRequest;
import dev.enco.webauth.backend.minecraft.enums.JoinRequestStatus;
import dev.enco.webauth.backend.minecraft.exception.JoinRequestNotFoundException;
import dev.enco.webauth.backend.minecraft.repository.JoinRequestRepository;
import dev.enco.webauth.backend.security.service.CurrentUserService;
import dev.enco.webauth.backend.shared.exception.UserNotFoundException;
import dev.enco.webauth.backend.trustedip.repository.TrustedIpRepository;
import dev.enco.webauth.backend.user.entity.User;
import dev.enco.webauth.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinecraftService {

    private final JoinRequestRepository joinRequestRepository;
    private final CurrentUserService currentUserService;
    private final TrustedIpRepository trustedIpRepository;
    private final UserRepository userRepository;
    private final JoinRequestEventPublisher eventPublisher;

    public CreateJoinResponse createJoinRequest(CreateJoinRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с указаным именем не найден!"));

        boolean trusted = trustedIpRepository.existsByUsernameAndIp(user.getUsername(), request.ip());

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername(user.getUsername());
        joinRequest.setIp(request.ip());
        joinRequest.setCountry(request.country());
        joinRequest.setCity(request.city());
        joinRequest.setRequestedAt(Instant.now());
        joinRequest.setStatus(trusted ? JoinRequestStatus.AUTO_APPROVED : JoinRequestStatus.PENDING);

        joinRequestRepository.save(joinRequest);

        return new CreateJoinResponse(
                joinRequest.getId(),
                joinRequest.getStatus().name()
        );
    }

    public List<JoinRequestResponse> getPendingJoinRequests(String accessToken) {
        User user = currentUserService.getByAccessToken(accessToken);

        return joinRequestRepository
                .findAllByUsernameAndStatus(user.getUsername(), JoinRequestStatus.PENDING)
                .stream()
                .map(item -> new JoinRequestResponse(
                        item.getId(),
                        item.getIp(),
                        item.getCountry(),
                        item.getCity(),
                        item.getRequestedAt(),
                        item.getStatus()
                ))
                .toList();
    }

    public void approveJoinRequest(String accessToken, Long requestId) {
        processJoinRequest(accessToken, requestId, JoinRequestStatus.APPROVED);
    }

    public void declineJoinRequest(String accessToken, Long requestId) {
        processJoinRequest(accessToken, requestId, JoinRequestStatus.DECLINED);
    }

    private void processJoinRequest(String accessToken, Long requestId, JoinRequestStatus status) {
        User user = currentUserService.getByAccessToken(accessToken);

        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new JoinRequestNotFoundException("Запрос на вход не найден"));

        validateOwnership(user, request);

        request.setStatus(status);
        joinRequestRepository.save(request);
        eventPublisher.publish(request.getId(), request.getUsername(), request.getStatus().name());
    }

    private void validateOwnership(User user, JoinRequest joinRequest) {
        if (!joinRequest.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("Этот join request не принадлежит текущему пользователю");
        }

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {
            throw new IllegalArgumentException("Join request уже обработан");
        }
    }
}
