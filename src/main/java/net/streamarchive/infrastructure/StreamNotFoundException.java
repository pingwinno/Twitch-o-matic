package net.streamarchive.infrastructure;

public class StreamNotFoundException extends Exception {

    public StreamNotFoundException(String message) {
        super(message);
    }
}

