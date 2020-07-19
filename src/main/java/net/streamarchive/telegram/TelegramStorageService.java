package net.streamarchive.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.models.TelegramFile;
import net.streamarchive.repository.TgChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class TelegramStorageService implements StorageService {


    @Autowired
    TelegramServerPool serverPool;
    @Autowired
    private TgChunkRepository tgChunkRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public long size(String streamPath, String fileName) {
        String[] path = streamPath.split("/");
        var streamerName = path[0];
        var uuid = UUID.fromString(path[1]);
        fileName = String.join("/", path[2], fileName);
        TelegramFile tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(uuid, streamerName, fileName);
        if (tgChunk == null) {
            return -1;
        }
        return tgChunk.getSize();
    }


    @Override
    public void write(InputStream inputStream, String streamPath, String fileName) throws IOException {
        String[] path = streamPath.split("/");
        var streamerName = path[0];
        var uuid = UUID.fromString(path[1]);
        fileName = String.join("/", path[2], fileName);
        TelegramFile tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(uuid, streamerName, fileName);
        if (tgChunk != null) {
            tgChunkRepository.delete(tgChunk);
        } else {
            tgChunk = new TelegramFile();
        }
        JsonNode jsonNode = objectMapper.readTree(serverPool.write(inputStream.readAllBytes()));
        tgChunk.setChunkName(fileName);
        tgChunk.setSize(jsonNode.get("size").asInt());
        tgChunk.setMessageID(jsonNode.get("id").asInt());
        tgChunk.setStreamer(streamerName);
        tgChunk.setUuid(uuid);
        tgChunkRepository.save(tgChunk);
    }


    @Override
    public InputStream read(String streamPath, String fileName) throws IOException {
        String[] path = streamPath.split("/");
        var streamerName = path[0];
        var uuid = UUID.fromString(path[1]);
        fileName = String.join("/", path[2], fileName);
        TelegramFile tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(uuid, streamerName, fileName);
        return serverPool.read(tgChunk.getMessageID());
    }

    @Override
    public void initialization() {

    }

    @Override
    public void deleteStream(UUID uuid, String streamer) {
        tgChunkRepository.deleteAllByUuidAndStreamer(uuid, streamer);
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
