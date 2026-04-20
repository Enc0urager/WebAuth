package dev.enco.webauth.backend.auth.exceptions;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
