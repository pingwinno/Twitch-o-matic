package com.pingwinno.application.twitch.playlist.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class MediaPlaylistParser {
    public static LinkedHashSet<String> parse (BufferedReader reader) throws IOException {
        String string;
        LinkedHashSet<String> chunks = new LinkedHashSet<>();
        while ((string = reader.readLine()) != null) {
            if (string.contains("#EXTINF")) {
                chunks.add(reader.readLine());
            }
        }
        return chunks;
    }
}
