package com.pingwinno.application.twitch.playlist.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class MediaPlaylistParser {
    public static LinkedHashSet<String> parse(BufferedReader reader) throws IOException {
        String header;
        String chunk;
        LinkedHashSet<String> chunks = new LinkedHashSet<>();
        while ((header = reader.readLine()) != null) {
            if (header.contains("#EXTINF")) {
                chunk = reader.readLine();
                if (!chunk.contains("muted")) {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }
}
