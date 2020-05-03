package net.streamarchive.infrastructure.data.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.TgChunk;
import net.streamarchive.repository.TgChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class TelegramHandler implements DataHandler {

    @Autowired
    private TgChunkRepository tgChunkRepository;

    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public long size(StreamDataModel stream, String fileName) {
        TgChunk tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(stream.getUuid(), stream.getStreamerName(), fileName);
        if (tgChunk == null) {
            return -1;
        }
        return tgChunk.getSize();
    }

    @SneakyThrows
    @Override
    public void write(InputStream inputStream, StreamDataModel stream, String fileName) {
        HttpEntity<byte[]> requestEntity =
                new HttpEntity<>(inputStream.readAllBytes());
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:10000",
                HttpMethod.POST,
                requestEntity,
                String.class);
        TgChunk tgChunk = new TgChunk();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        tgChunk.setChunkName(fileName);
        tgChunk.setSize(jsonNode.get("size").asInt());
        tgChunk.setMessageID(jsonNode.get("id").asInt());
        tgChunk.setStreamer(stream.getStreamerName());
        tgChunk.setUuid(stream.getUuid());
        tgChunkRepository.save(tgChunk);
    }

    @SneakyThrows
    @Override
    public InputStream read(StreamDataModel stream, String fileName) {
        TgChunk tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(stream.getUuid(), stream.getStreamerName(), fileName);
        return new URL("http://localhost:10000" + "/" + tgChunk.getMessageID()).openStream();
    }

    @Override
    public void initialization() {

    }

    @Override
    public UUID getUUID() {
        UUID uuid = UUID.randomUUID();
        if (tgChunkRepository.existsByUuid(uuid)) {
            //generate uuid again
            uuid = getUUID();
        }
        return uuid;
    }
}
