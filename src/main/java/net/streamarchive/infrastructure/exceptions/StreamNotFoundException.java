package net.streamarchive.infrastructure.exceptions;

public class StreamNotFoundException extends RuntimeException {

    public StreamNotFoundException(String message) {
        super(message);
    }
}

