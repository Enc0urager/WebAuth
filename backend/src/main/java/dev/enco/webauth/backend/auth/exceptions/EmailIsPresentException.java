package dev.enco.webauth.backend.auth.exceptions;

public class EmailIsPresentException extends RuntimeException {
    public EmailIsPresentException(String message) {
        super(message);
    }
}
