package dev.enco.webauth.backend.shared.exception;

import dev.enco.webauth.backend.auth.exceptions.EmailIsPresentException;
import dev.enco.webauth.backend.auth.exceptions.IncorrectPasswordException;
import dev.enco.webauth.backend.auth.exceptions.InvalidVerificationCodeException;
import dev.enco.webauth.backend.security.exception.AntiBruteforceException;
import dev.enco.webauth.backend.security.exception.RateLimitException;
import dev.enco.webauth.backend.shared.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse build(HttpServletRequest request, String message, HttpStatus status) {
        return new ErrorResponse(
                message,
                status.value(),
                request.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(build(request, ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(EmailIsPresentException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(
            EmailIsPresentException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(build(request, ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            IncorrectPasswordException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(build(request, ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCode(
            InvalidVerificationCodeException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(request, ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AntiBruteforceException.class)
    public ResponseEntity<ErrorResponse> handleBruteforce(
            AntiBruteforceException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(build(request, ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS));
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimit(
            RateLimitException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(build(request, ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwt(
            JwtException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(build(request, "Invalid token", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(request, ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(build(request, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
