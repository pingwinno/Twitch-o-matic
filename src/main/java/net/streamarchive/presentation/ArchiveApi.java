package net.streamarchive.presentation;

import net.streamarchive.infrastructure.data.handler.TelegramHandler;
import net.streamarchive.infrastructure.models.TgChunk;
import net.streamarchive.repository.TgChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("streams")
public class ArchiveApi {
    @Autowired
    private TgChunkRepository tgChunkRepository;

    @GetMapping(path = "{streamer}/{uuid}/chunked/{file}")
    public @ResponseBody
    ResponseEntity<byte[]> getFile(@PathVariable("streamer") String streamer, @PathVariable("uuid") UUID uuid, @PathVariable("file") String file) throws IOException {
        TgChunk tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(uuid, streamer, file);
        URL website = new URL(TelegramHandler.TGSERVER_ADDRESS + "/" + tgChunk.getMessageID());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
                .contentLength(tgChunk.getSize())
                .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                .body(website.openStream().readAllBytes());
    }

    @GetMapping(path = "{streamer}/{uuid}/preview.jpg")
    public @ResponseBody
    ResponseEntity<byte[]> getPreview(@PathVariable("streamer") String streamer, @PathVariable("uuid") UUID uuid) throws IOException {
        TgChunk tgChunk = tgChunkRepository.findByUuidAndStreamerAndChunkName(uuid, streamer, "preview.jpg");
        URL website = new URL(TelegramHandler.TGSERVER_ADDRESS + "/" + tgChunk.getMessageID());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "preview.jpg")
                .contentLength(tgChunk.getSize())
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(website.openStream().readAllBytes());
    }
}
