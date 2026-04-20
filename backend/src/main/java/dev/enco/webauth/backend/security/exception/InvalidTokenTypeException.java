package dev.enco.webauth.backend.security.exception;

public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String message) {
        super(message);
    }
}
