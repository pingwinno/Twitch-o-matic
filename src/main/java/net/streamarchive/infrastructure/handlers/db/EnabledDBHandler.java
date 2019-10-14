package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class EnabledDBHandler implements ArchiveDBHandler {
    static private org.slf4j.Logger log = LoggerFactory.getLogger(EnabledDBHandler.class);
    @Autowired
    JpaDBHandler jpaDBHandler;
    @Autowired
    LocalDBHandler localDBHandler;
    @Autowired
    RestDBHandler restDBHandler;

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
        try {
            restDBHandler.addStream(stream);
        } catch (Exception e) {
            log.error("Can't write to remote db", e);
        }
        jpaDBHandler.addStream(stream);
        localDBHandler.addStream(stream);
    }

    @Override
    public void updateStream(Stream stream) throws StreamerNotFoundException, StreamNotFoundException {
        try {
            restDBHandler.updateStream(stream);
        } catch (Exception e) {
            log.error("Can't write to remote db", e);
        }
        jpaDBHandler.updateStream(stream);
        localDBHandler.updateStream(stream);
    }

    @Override
    public void deleteStream(Stream stream) throws StreamNotFoundException {
        try {
            restDBHandler.deleteStream(stream);
        } catch (Exception e) {
            log.error("Can't write to remote db", e);
        }
        jpaDBHandler.deleteStream(stream);
        localDBHandler.deleteStream(stream);
    }
}
