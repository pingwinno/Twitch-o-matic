package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import net.streamarchive.repository.StreamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JpaDBHandler implements ArchiveDBHandler {
    @Autowired
    StreamsRepository streamsRepository;

    @Override
    public List<Stream> getAllStreams(String streamer) throws StreamerNotFoundException {
        List<Stream> streams = streamsRepository.findAllByStreamer(streamer);
        if (streams.isEmpty()) throw new StreamerNotFoundException("Streamer " + streamer + " not found");
        return streams;
    }

    @Override
    public Stream getStream(String streamer, UUID uuid) throws StreamNotFoundException {
        return streamsRepository.findById(uuid).orElseThrow(() -> new StreamNotFoundException("Stream "
                + uuid + " not found"));

    }

    @Override
    public void addStream(Stream stream) {
        streamsRepository.save(stream);
    }

    @Override
    public void updateStream(Stream stream) {
        streamsRepository.save(stream);
    }

    @Override
    public void deleteStream(Stream stream) throws StreamNotFoundException {
        if (!streamsRepository.existsById(stream.getUuid()))
            throw new StreamNotFoundException("Stream " + stream.getUuid().toString() + " not found");
        streamsRepository.deleteById(stream.getUuid());
    }

}
