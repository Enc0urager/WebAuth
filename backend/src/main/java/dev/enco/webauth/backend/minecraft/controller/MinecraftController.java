package dev.enco.webauth.backend.minecraft.controller;

import dev.enco.webauth.backend.minecraft.dto.CreateJoinRequest;
import dev.enco.webauth.backend.minecraft.dto.CreateJoinResponse;
import dev.enco.webauth.backend.minecraft.service.MinecraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal/minecraft")
@RequiredArgsConstructor
public class MinecraftController {

    private final MinecraftService minecraftService;

    @PostMapping("/join-request")
    public CreateJoinResponse createJoinRequest(@RequestBody CreateJoinRequest request) {
        return minecraftService.createJoinRequest(request);
    }
}
