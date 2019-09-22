package net.streamarchive.infrastructure.handlers.db;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LocalDBHandler implements ArchiveDBHandler {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(LocalDBHandler.class);
    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<Stream> getAllStreams(String streamer) throws StreamerNotFoundException {
        List<Stream> streams = new ArrayList<>();
        try (java.util.stream.Stream<Path> paths = Files.walk(Paths.get(settingsProperties.getRecordedStreamPath() + streamer))) {
            List<String> result = paths.filter(Files::isDirectory)
                    .map(Path::toString).collect(Collectors.toList());
            result.forEach(stream -> {
                try {
                    streams.add(new ObjectMapper().readValue(stream + "/metadata.json", Stream.class));
                } catch (IOException e) {
                    log.error("Stream " + stream + " loading failed", e);
                }
            });
        } catch (IOException e) {
            log.error("Streams of " + streamer + " loading failed", e);
            throw new StreamerNotFoundException("Streams of " + streamer + " loading failed");
        }
        return streams;
    }

    @Override
    public Stream getStream(String streamer, UUID uuid) throws StreamNotFoundException {
        try {
            return new ObjectMapper().readValue(
                    new File(settingsProperties.getRecordedStreamPath() + streamer + "/" + uuid.toString() + "/metadata.json"), Stream.class);
        } catch (IOException e) {
            log.error("Streams of " + streamer + " loading failed", e);
            throw new StreamNotFoundException(uuid + " metadata loading failed");
        }
    }

    @Override
    public void addStream(Stream stream) throws IOException {

            if (!Files.exists(Paths.get(settingsProperties.getRecordedStreamPath() + stream.getStreamer() + "/" + stream.getUuid()
                    + "/metadata.json"))) {
                File file = new File(settingsProperties.getRecordedStreamPath() + stream.getStreamer() + "/" + stream.getUuid()
                        + "/metadata.json");
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

                writer.writeValue(file, stream);

            } else log.error("Stream" + stream.getUuid() + "exist");


    }

    @Override
    public void updateStream(Stream stream) throws StreamNotFoundException, StreamerNotFoundException {
        if (Files.exists(Paths.get(settingsProperties.getRecordedStreamPath() + stream.getUuid()
                + "/metadata.json"))) {

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            try (FileOutputStream output = new FileOutputStream(new File(settingsProperties.getRecordedStreamPath()
                    + stream.getStreamer() + "/" + stream.getUuid()
                    + "/metadata.json"), false);) {
                writer.writeValue(output, stream);
            } catch (FileNotFoundException e) {
                log.error("Folder of streamer " + stream.getStreamer() + " not found");
                throw new StreamerNotFoundException("Folder of streamer " + stream.getStreamer() + " not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw new StreamNotFoundException("Stream" + stream.getUuid() + "not exist");
    }

    @Override
    public void deleteStream(Stream stream) throws StreamNotFoundException {
        try {
            Files.delete(Paths.get(settingsProperties.getRecordedStreamPath() + stream.getStreamer() + "/" + stream.getUuid().toString()
                    + "/metadata.json"));
        } catch (IOException e) {
            log.error("Stream " + stream.getUuid().toString() + " deleting failed.", e);
            throw new StreamNotFoundException("Stream " + stream.getUuid().toString() + " deleting failed.");
        }
    }
}
