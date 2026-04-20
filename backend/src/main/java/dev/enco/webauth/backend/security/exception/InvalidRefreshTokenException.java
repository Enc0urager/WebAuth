package dev.enco.webauth.backend.security.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
