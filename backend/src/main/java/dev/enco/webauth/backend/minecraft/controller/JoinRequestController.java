package dev.enco.webauth.backend.minecraft.controller;

import dev.enco.webauth.backend.minecraft.dto.JoinRequestResponse;
import dev.enco.webauth.backend.minecraft.service.MinecraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/join-requests")
@RequiredArgsConstructor
public class JoinRequestController {
    private final MinecraftService minecraftService;

    @GetMapping
    public List<JoinRequestResponse> getJoinRequests(@RequestHeader("Authorization") String header) {
        return minecraftService.getPendingJoinRequests(extractToken(header));
    }

    @PostMapping("/join-requests/{id}/approve")
    public void approveJoinRequest(@RequestHeader("Authorization") String header,
                                   @PathVariable Long id) {
        minecraftService.approveJoinRequest(extractToken(header), id);
    }

    @PostMapping("/join-requests/{id}/decline")
    public void declineJoinRequest(@RequestHeader("Authorization") String header,
                                   @PathVariable Long id) {
        minecraftService.declineJoinRequest(extractToken(header), id);
    }

    private String extractToken(String header) {
        return header.substring(7);
    }
}
