package com.spaik.backend.presentation.ai.exception;

public class AiException extends RuntimeException {
    public AiException(String message) {
        super(message);
    }

    public AiException(String message, Throwable cause) {
        super(message, cause);
    }
}
