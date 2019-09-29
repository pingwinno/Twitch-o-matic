package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class RestDBHandler implements ArchiveDBHandler {
    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    private RestTemplate restTemplateWithCredentials;

    @Override
    public List<Stream> getAllStreams(String streamer) throws StreamerNotFoundException {

        ResponseEntity<List<Stream>> response = restTemplateWithCredentials.exchange(
                settingsProperties.getRemoteDBAddress() + "streams/" + streamer,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        if (response.getStatusCode().value() == 404) {
            throw new StreamerNotFoundException(streamer + " not found");
        }
        return response.getBody();
    }

    @Override
    public Stream getStream(String streamer, UUID uuid) throws StreamNotFoundException {
        ResponseEntity<Stream> response = restTemplateWithCredentials.exchange(
                settingsProperties.getRemoteDBAddress() + "streams/" + streamer + "/" + uuid,
                HttpMethod.GET,
                null, Stream.class);
        if (response.getStatusCode().value() == 404) {
            throw new StreamNotFoundException("Streams of " + streamer + " not found");
        }
        return response.getBody();
    }

    @Override
    public void addStream(Stream stream) {
        restTemplateWithCredentials.postForObject(settingsProperties.getRemoteDBAddress() + "streams/" + stream.getStreamer(), stream, Stream.class);

    }

    @Override
    public void updateStream(Stream stream) {
        restTemplateWithCredentials.postForObject(settingsProperties.getRemoteDBAddress() + "streams/" + stream.getStreamer(), stream, Stream.class);
    }

    @Override
    public void deleteStream(Stream stream) throws StreamNotFoundException {
        restTemplateWithCredentials.delete(settingsProperties.getRemoteDBAddress() + "streams/" + stream.getStreamer() + "/" + stream.getUuid().toString());
    }

}
