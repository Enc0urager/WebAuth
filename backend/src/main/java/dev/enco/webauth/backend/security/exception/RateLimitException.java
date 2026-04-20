package dev.enco.webauth.backend.security.exception;

public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}
