package net.streamarchive.infrastructure.models;

public class StreamerNotFoundException extends Exception {
    public StreamerNotFoundException(String message) {
        super(message);
    }
}
