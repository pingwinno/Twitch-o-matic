package net.streamarchive.telegram;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.data.handler.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

import static net.streamarchive.util.UrlFormatter.format;

@Slf4j
@Service
public class WebStorageService implements StorageService {

    @Autowired
    private RestTemplate restTemplateForWebStorage;

    @Value("${net.streamarchive.webstorage.address}")
    private String serverAddress;

    @Override
    public Long size(String streamPath, String fileName) {
        log.trace("Stream path: {}/{}", streamPath, fileName);

        ResponseEntity<Long> response = restTemplateForWebStorage.exchange(format(serverAddress, "size", streamPath, fileName), HttpMethod.GET,
                null,
                Long.class);
        return response.getBody();
    }


    @Override
    public void write(InputStream inputStream, String streamPath, String fileName) throws IOException {
        log.trace("Stream path: {}/{}", streamPath, fileName);
        write(inputStream.readAllBytes(), format(streamPath, fileName));
    }


    @Override
    public InputStream read(String streamPath, String fileName) {
        throw new UnsupportedOperationException();
    }

    public String write(byte[] data, String path) {
        HttpEntity<byte[]> requestEntity =
                new HttpEntity<>(data);
        ResponseEntity<String> response = restTemplateForWebStorage.exchange(format(serverAddress, path), HttpMethod.PUT,
                requestEntity,
                String.class);
        return response.getBody();
    }

    @Override
    public void initialization() {

    }

    @Override
    public void initStreamStorage(Collection<String> qualities, String path) throws IOException {

    }

    @Override
    public void deleteStream(UUID uuid, String streamer) {

    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }
}
