package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Repository
public interface ArchiveDBHandler {
    List<Stream> getAllStreams(String streamer) throws StreamerNotFoundException;

    Stream getStream(String streamer, UUID uuid) throws StreamNotFoundException;

    void addStream(Stream stream) throws IOException;

    void updateStream(Stream stream) throws StreamerNotFoundException, StreamNotFoundException;

    void deleteStream(Stream stream) throws StreamNotFoundException;
}
