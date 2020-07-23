package net.streamarchive.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.models.TelegramFile;
import net.streamarchive.repository.TgChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class TelegramStorageService implements StorageService {


    @Autowired
    TelegramServerPool serverPool;
    @Autowired
    private TgChunkRepository tgChunkRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public long size(String streamPath, String fileName) {
        log.trace("Stream path: {}/{}", streamPath,fileName);
        String path = streamPath+fileName;
        log.trace("File path: {}", path);
        TelegramFile tgChunk = tgChunkRepository.findByPath(path);
        if (tgChunk == null) {
            return -1;
        }
        return tgChunk.getSize();
    }


    @Override
    public void write(InputStream inputStream, String streamPath, String fileName) throws IOException {
        log.trace("Stream path: {}/{}", streamPath,fileName);
        String path = streamPath+fileName;

        log.trace("File path: {}", path);
        TelegramFile tgChunk = tgChunkRepository.findByPath(path);
        if (tgChunk != null) {
            tgChunkRepository.delete(tgChunk);
        } else {
            tgChunk = new TelegramFile();
        }
        JsonNode jsonNode = objectMapper.readTree(serverPool.write(inputStream.readAllBytes()));
        tgChunk.setPath(path);
        tgChunk.setSize(jsonNode.get("size").asInt());
        tgChunk.setMessageID(jsonNode.get("id").asInt());
        var returnedRecord = tgChunkRepository.save(tgChunk);
        log.trace("TgChunck {}", returnedRecord);
    }


    @Override
    public InputStream read(String streamPath, String fileName) throws IOException {
        String path = streamPath+fileName;
        log.trace("File path: {}", path);
        TelegramFile tgChunk = tgChunkRepository.findByPath(path);
        return serverPool.read(tgChunk.getMessageID());
    }

    @Override
    public void initialization() {

    }

    @Override
    public void deleteStream(UUID uuid, String streamer) {

    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }
}
