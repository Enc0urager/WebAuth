package dev.enco.webauth.backend.minecraft.exception;

public class JoinRequestNotFoundException extends RuntimeException {
    public JoinRequestNotFoundException(String message) {
        super(message);
    }
}
