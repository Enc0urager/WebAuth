package dev.enco.webauth.backend.minecraft.dto;

public record CreateJoinRequest(
    String username,
    String ip,
    String country,
    String city
) {}
