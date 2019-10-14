package net.streamarchive.application.twitch.handler;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MediaPlaylistParser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());

    public static LinkedHashMap<String, Double> getChunks(BufferedReader reader, boolean skipMuted) throws IOException {
        String header;
        String chunk;
        LinkedHashMap<String, Double> chunks = new LinkedHashMap<>();
        while ((header = reader.readLine()) != null) {
            log.trace(header);
            if (header.contains("#EXTINF")) {
                chunk = reader.readLine();
                log.trace(chunk);
                String replace = header.substring(header.lastIndexOf(":") + 1)
                        .replace(",", "");
                if (skipMuted && !chunk.contains("muted")) {

                    chunks.put(chunk, Double.parseDouble(replace));
                } else if (!skipMuted) {

                    chunks.put(chunk, Double.parseDouble(replace));
                }

            }
        }
        reader.close();
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
        reader.close();
        return time;
    }
}
