package dev.enco.webauth.backend.security.exception;

public class AntiBruteforceException extends RuntimeException {
    public AntiBruteforceException(String message) {
        super(message);
    }
}
