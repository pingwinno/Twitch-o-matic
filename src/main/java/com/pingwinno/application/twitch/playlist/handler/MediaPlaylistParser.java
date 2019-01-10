package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.models.ChunkModel;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class MediaPlaylistParser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());

    public static LinkedHashSet<ChunkModel> getChunks(BufferedReader reader, boolean skipMuted) throws IOException {
        String header;
        String chunk;
        LinkedHashSet<ChunkModel> chunks = new LinkedHashSet<>();
        while ((header = reader.readLine()) != null) {
            log.trace(header);
            if (header.contains("#EXTINF")) {
                chunk = reader.readLine();
                log.trace(chunk);
                String replace = header.substring(header.lastIndexOf(":") + 1)
                        .replace(",", "");
                if (skipMuted && !chunk.contains("muted")) {

                    chunks.add(new ChunkModel(chunk, Double.parseDouble(replace)));
                } else if (!skipMuted) {

                    chunks.add(new ChunkModel(chunk, Double.parseDouble(replace)));
                }

            }
        }
        return chunks;
    }

    public static long getTotalSec(BufferedReader reader) throws IOException {
        String header;
        long time = 0;
        while ((header = reader.readLine()) != null) {
            log.trace(header);
            if (header.contains("#EXT-X-TWITCH-TOTAL-SECS")) {
                time = (long) Double.parseDouble(header.substring(header.lastIndexOf(":") + 1));
                log.trace("Stream duration: {}", time);
            }
        }
        return time;
    }
}
