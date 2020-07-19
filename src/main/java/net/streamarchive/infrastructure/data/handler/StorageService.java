package net.streamarchive.infrastructure.data.handler;

import net.streamarchive.infrastructure.models.StreamDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.UUID;

public interface StorageService {
    long size(String streamPath, String fileName) throws IOException;

    void write(InputStream inputStream, String streamPath, String fileName) throws IOException;

    InputStream read(String streamPath, String fileName) throws IOException;

    void initialization();

    void deleteStream(UUID uuid, String streamer);

    UUID getUUID();
}
