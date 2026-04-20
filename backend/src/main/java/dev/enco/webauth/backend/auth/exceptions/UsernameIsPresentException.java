package dev.enco.webauth.backend.auth.exceptions;

public class UsernameIsPresentException extends RuntimeException {
    public UsernameIsPresentException(String message) {
        super(message);
    }
}
