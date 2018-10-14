package com.pingwinno.application.twitch.playlist.handler;


import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


public class MasterPlaylistParser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MasterPlaylistParser.class.getName());

    public static String parse(BufferedReader reader) throws IOException {
        String string;
        String m3u8Link = null;
        while ((string = reader.readLine()) != null) {
            log.trace(string);
            if (string.contains("VIDEO=\"" + SettingsProperties.getStreamQuality() + "\"")) {
                m3u8Link = reader.readLine();
            }
        }
        return m3u8Link;
    }
}
