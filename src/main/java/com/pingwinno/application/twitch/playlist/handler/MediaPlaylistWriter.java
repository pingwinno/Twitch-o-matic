package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.models.ChunkModel;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;

public class MediaPlaylistWriter {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());

    public static void write(LinkedHashSet<ChunkModel> playlist, String streamFolderPath) throws IOException {
        FileWriter fstream = new FileWriter(streamFolderPath + "/index-dvr.m3u8");
        BufferedWriter out = new BufferedWriter(fstream);
        log.debug("Writing playlist...");
        out.write("#EXTM3U\n" +
                "#EXT-X-VERSION:3\n" +
                "#EXT-X-TARGETDURATION:10\n" +
                "#EXT-X-PLAYLIST-TYPE:EVENT\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n");
        for (ChunkModel chunk : playlist) {

            if (chunk.getChunkName().contains("muted")) {
                log.trace("muted line {}", chunk.getChunkName());
                out.write("#EXTINF:" + String.format("%.3f", chunk.getTime()) + ",\n");
                out.write(chunk.getChunkName().replace("-muted", ""));
                out.newLine();
            } else {
                log.trace("simple line {}", chunk.getChunkName());
                out.write("#EXTINF:" + String.format("%.3f", chunk.getTime()) + ",\n");
                out.write(chunk.getChunkName());
                out.newLine();
            }

        }
        out.write("#EXT-X-ENDLIST");
        out.close();
        fstream.close();
        log.debug("done");

    }
}

