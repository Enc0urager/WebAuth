package dev.enco.webauth.backend.security.exception;

public class UsedTokenException extends RuntimeException {
    public UsedTokenException(String message) {
        super(message);
    }
}
