package net.streamarchive.infrastructure.exceptions;

public class TwitchTokenProcessingException extends RuntimeException {
    public TwitchTokenProcessingException() {
        super();
    }

    public TwitchTokenProcessingException(String message) {
        super(message);
    }

    public TwitchTokenProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitchTokenProcessingException(Throwable cause) {
        super(cause);
    }

    protected TwitchTokenProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
