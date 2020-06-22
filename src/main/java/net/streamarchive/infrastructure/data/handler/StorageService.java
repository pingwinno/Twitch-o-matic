package net.streamarchive.infrastructure.data.handler;

import net.streamarchive.infrastructure.models.StreamDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.UUID;

public interface StorageService {
    long size(StreamDataModel stream, String fileName) throws IOException;

    void write(InputStream inputStream, StreamDataModel stream, String fileName) throws IOException;

    InputStream read(StreamDataModel stream, String fileName) throws IOException;

    void initialization();

    void deleteStream(UUID uuid, String streamer);

    UUID getUUID();
}
