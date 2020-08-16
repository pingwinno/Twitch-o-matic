package net.streamarchive.infrastructure.data.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StorageService {
    Long size(String streamPath, String fileName) throws IOException;

    void write(InputStream inputStream, String streamPath, String fileName) throws IOException;

    InputStream read(String streamPath, String fileName) throws IOException;

    void initialization();

    void initStreamStorage(Collection<String> qualities,String path) throws IOException;

    void deleteStream(UUID uuid, String streamer);

    UUID getUUID();
}
