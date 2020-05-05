package net.streamarchive.infrastructure.exceptions;

public class NotAcceptableException extends RuntimeException {
    public NotAcceptableException() {
    }

    public NotAcceptableException(String message) {
        super(message);
    }

    public NotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAcceptableException(Throwable cause) {
        super(cause);
    }

    public NotAcceptableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
