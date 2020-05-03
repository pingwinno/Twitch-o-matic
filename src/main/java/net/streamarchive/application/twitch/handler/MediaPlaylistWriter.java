package net.streamarchive.application.twitch.handler;

import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class MediaPlaylistWriter {
    private org.slf4j.Logger log = LoggerFactory.getLogger(MediaPlaylistParser.class.getName());

    public InputStream write(LinkedHashMap<String, Double> playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        log.debug("Writing playlist...");
        stringBuilder.append("#EXTM3U\n" +
                "#EXT-X-VERSION:3\n" +
                "#EXT-X-TARGETDURATION:10\n" +
                "#EXT-X-PLAYLIST-TYPE:EVENT\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n");
        for (Map.Entry<String, Double> chunk : playlist.entrySet()) {

            if (chunk.getKey().contains("muted")) {
                log.trace("muted line {}", chunk.getKey());
                stringBuilder.append("#EXTINF:" + String.format("%.3f", chunk.getValue()) + ",\n");
                stringBuilder.append(chunk.getKey().replace("-muted", ""));
                stringBuilder.append("\n");
            } else {
                log.trace("simple line {}", chunk.getKey());
                stringBuilder.append("#EXTINF:" + String.format("%.3f", chunk.getValue()) + ",\n");
                stringBuilder.append(chunk.getKey());
                stringBuilder.append("\n");
            }

        }
        stringBuilder.append("#EXT-X-ENDLIST");
        stringBuilder.append("\n");
        log.debug("done");
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

}

