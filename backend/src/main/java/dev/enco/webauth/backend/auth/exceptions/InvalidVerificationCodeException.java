package dev.enco.webauth.backend.auth.exceptions;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
