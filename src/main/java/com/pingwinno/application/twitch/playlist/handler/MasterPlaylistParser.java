package com.pingwinno.application.twitch.playlist.handler;


import java.io.BufferedReader;
import java.io.IOException;


public class MasterPlaylistParser {
    public static String parse(BufferedReader reader) throws IOException {
        String string;
        String m3u8Link = null;
        while ((string = reader.readLine()) != null) {
            if (string.contains("VIDEO=\"chunked\"")) {
                m3u8Link = reader.readLine();
            }
        }
        return m3u8Link;
    }
}
