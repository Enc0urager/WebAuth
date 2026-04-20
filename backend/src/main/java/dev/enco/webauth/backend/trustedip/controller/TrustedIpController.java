package dev.enco.webauth.backend.trustedip.controller;

import dev.enco.webauth.backend.trustedip.dto.AddTrustedIpRequest;
import dev.enco.webauth.backend.trustedip.dto.TrustedIpResponse;
import dev.enco.webauth.backend.trustedip.service.TrustedIpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trusted-ips")
@RequiredArgsConstructor
public class TrustedIpController {

    private final TrustedIpService trustedIpService;

    @GetMapping
    public List<TrustedIpResponse> getTrustedIps(@RequestHeader("Authorization") String header) {
        return trustedIpService.getTrustedIps(extractToken(header));
    }

    @PostMapping
    public void addTrustedIp(@RequestHeader("Authorization") String header,
                             @RequestBody AddTrustedIpRequest request) {
        trustedIpService.addTrustedIp(extractToken(header), request);
    }

    @DeleteMapping("/{id}")
    public void removeTrustedIp(@RequestHeader("Authorization") String header,
                                @PathVariable Long id) {
        trustedIpService.removeTrustedIp(extractToken(header), id);
    }

    private String extractToken(String header) {
        return header.substring(7);
    }
}
