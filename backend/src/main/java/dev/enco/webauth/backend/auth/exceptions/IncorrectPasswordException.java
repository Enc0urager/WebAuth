package dev.enco.webauth.backend.auth.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
