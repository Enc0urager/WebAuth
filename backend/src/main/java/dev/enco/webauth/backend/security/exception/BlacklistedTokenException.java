package dev.enco.webauth.backend.security.exception;

public class BlacklistedTokenException extends RuntimeException {
    public BlacklistedTokenException(String message) {
        super(message);
    }
}
