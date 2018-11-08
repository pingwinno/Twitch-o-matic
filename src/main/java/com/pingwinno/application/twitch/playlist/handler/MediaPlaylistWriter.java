package com.pingwinno.application.twitch.playlist.handler;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MediaPlaylistWriter {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());

    public static void write(BufferedReader reader, String streamFolderPath) throws IOException {
        String line;

        FileWriter fstream = new FileWriter(streamFolderPath + "/index-dvr.m3u8");
        BufferedWriter out = new BufferedWriter(fstream);
        log.debug("Writing playlist...");

        while ((line = reader.readLine()) != null) {

            if (line.contains("muted")) {
                log.trace("muted line {}", line);

                out.write(line.replace("-muted", ""));
                out.newLine();
            } else {
                log.trace("simple line {}", line);
                out.write(line);
                out.newLine();
            }

        }
        out.close();
        fstream.close();
        log.debug("done");

    }
}

