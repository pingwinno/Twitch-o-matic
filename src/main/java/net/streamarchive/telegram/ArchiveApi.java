package net.streamarchive.telegram;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.telegram.TelegramServerPool;
import net.streamarchive.infrastructure.models.TelegramFile;
import net.streamarchive.repository.TgChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("streams")
public class ArchiveApi {
    @Autowired
    private TgChunkRepository tgChunkRepository;

    @Autowired
    private TelegramServerPool telegramServerPool;

    @GetMapping(path = "{streamer}/{uuid}/{file}")
    public @ResponseBody
    ResponseEntity<byte[]> getFile(@PathVariable("streamer") String streamer, @PathVariable("uuid") UUID uuid, @PathVariable("file") String file) throws IOException {
        log.trace("Query path: {}/{}/{}",streamer,uuid,file);

        TelegramFile tgChunk = tgChunkRepository.findByPath(String.join("/",streamer,uuid.toString(),file));
        log.trace("TgChunk: {}",tgChunk);
        URL website = new URL(telegramServerPool.getAddress(false) + "/" + tgChunk.getMessageID());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
                .contentLength(tgChunk.getSize())
                .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                .body(website.openStream().readAllBytes());
    }

    @GetMapping(path = "{streamer}/{uuid}/{quality}/{file}")
    public @ResponseBody
    ResponseEntity<byte[]> getFileWithQuality(@PathVariable("streamer") String streamer, @PathVariable("uuid") String uuid, @PathVariable("file") String file,@PathVariable("quality") String quality) throws IOException {
        log.trace("Query path: {}/{}/{}",streamer,uuid,file);

        TelegramFile tgChunk = tgChunkRepository.findByPath(String.join("/",streamer,uuid,quality,file));
        log.trace("TgChunk: {}",tgChunk);
        URL website = new URL(telegramServerPool.getAddress(false) + "/" + tgChunk.getMessageID());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
                .contentLength(tgChunk.getSize())
                .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                .body(website.openStream().readAllBytes());
    }

}
