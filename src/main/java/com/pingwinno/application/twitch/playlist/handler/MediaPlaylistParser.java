package com.pingwinno.application.twitch.playlist.handler;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class MediaPlaylistParser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());
    public static LinkedHashSet<String> parse(BufferedReader reader, boolean skipMuted) throws IOException {
        String header;
        String chunk;
        LinkedHashSet<String> chunks = new LinkedHashSet<>();
        while ((header = reader.readLine()) != null) {
            log.trace(header);
            if (header.contains("#EXTINF")) {
                chunk = reader.readLine();
                log.trace(chunk);
                if (skipMuted && !chunk.contains("muted")) {
                    chunks.add(chunk);
                } else {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }
}
