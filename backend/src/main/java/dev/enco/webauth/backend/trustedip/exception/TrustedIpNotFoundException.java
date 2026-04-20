package dev.enco.webauth.backend.trustedip.exception;

public class TrustedIpNotFoundException extends RuntimeException {
    public TrustedIpNotFoundException(String message) {
        super(message);
    }
}
