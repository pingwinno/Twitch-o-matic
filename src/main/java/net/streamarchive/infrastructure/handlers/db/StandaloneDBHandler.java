package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class StandaloneDBHandler implements ArchiveDBHandler {
    @Autowired
    JpaDBHandler jpaDBHandler;
    @Autowired
    LocalDBHandler localDBHandler;

    @Override
    public List<Stream> getAllStreams(String streamer) throws StreamerNotFoundException {
        return jpaDBHandler.getAllStreams(streamer);
    }

    @Override
    public Stream getStream(String streamer, UUID uuid) throws StreamNotFoundException {
        return jpaDBHandler.getStream(streamer, uuid);
    }

    @Override
    public void addStream(Stream stream) throws IOException {
        jpaDBHandler.addStream(stream);
        localDBHandler.addStream(stream);
    }

    @Override
    public void updateStream(Stream stream) throws StreamerNotFoundException, StreamNotFoundException {
        jpaDBHandler.updateStream(stream);
        localDBHandler.updateStream(stream);
    }

    @Override
    public void deleteStream(Stream stream) throws StreamNotFoundException {
        jpaDBHandler.deleteStream(stream);
        localDBHandler.deleteStream(stream);


    }
}
