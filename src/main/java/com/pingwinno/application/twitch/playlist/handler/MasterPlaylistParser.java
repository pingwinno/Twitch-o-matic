package com.pingwinno.application.twitch.playlist.handler;


import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


public class MasterPlaylistParser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MasterPlaylistParser.class.getName());

    public static String parse(BufferedReader reader, String streamQuality) throws IOException {
        String string;
        String m3u8Link = null;
        while ((string = reader.readLine()) != null) {
            log.trace(string);
            if (string.contains("VIDEO=\"" + streamQuality + "\"")) {
                m3u8Link = reader.readLine();
                log.trace(m3u8Link);
            }
        }
        reader.close();
        return m3u8Link;
    }
}
