package net.streamarchive.application.twitch.handler;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamFileModel;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public class MediaPlaylistParser {


    public static Set<StreamFileModel> getChunks(BufferedReader reader, String baseUrl, String basePath, boolean skipMuted) throws IOException {
        String header;
        String chunk;
        Set<StreamFileModel> chunks = new LinkedHashSet<>();
        while ((header = reader.readLine()) != null) {
            log.trace(header);
            if (header.contains("#EXTINF")) {
                chunk = reader.readLine();
                log.trace(chunk);
                String replace = header.substring(header.lastIndexOf(":") + 1)
                        .replace(",", "");
                if (skipMuted && !chunk.contains("muted")) {
                    chunks.add(new StreamFileModel(baseUrl, basePath, chunk, Double.parseDouble(replace)));
                } else if (!skipMuted) {
                    chunks.add(new StreamFileModel(baseUrl, basePath, chunk.replace("unmuted", "muted"), Double.parseDouble(replace)));
                }
            }
        }
        reader.close();
        return chunks;
    }

    public static long getTotalSec(Set<StreamFileModel> playlist) {
        return (long) playlist.stream().mapToDouble(StreamFileModel::getDuration).sum();
    }
}
