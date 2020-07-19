package net.streamarchive.application.twitch.handler;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.MasterPlaylistStrings;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamFileModel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

@Slf4j
public class PlaylistWriter {

    public static InputStream writeMedia(Set<StreamFileModel> playlist) {
        StringBuilder stringBuilder = new StringBuilder();
        log.debug("Writing playlist...");
        stringBuilder.append("#EXTM3U\n" +
                "#EXT-X-VERSION:3\n" +
                "#EXT-X-TARGETDURATION:10\n" +
                "#EXT-X-PLAYLIST-TYPE:EVENT\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n");
        playlist.forEach(file -> {
            var fileName = file.getFileName();
            var chunkDuration = file.getDuration();
            if (fileName.contains("muted")) {
                log.trace("muted line {}", file.getFileName());
                stringBuilder.append("#EXTINF:").append(String.format("%.3f", file.getDuration())).append(",\n");
                stringBuilder.append(fileName.replace("-muted", ""));
                stringBuilder.append("\n");
            } else {
                log.trace("simple line {}", chunkDuration);
                stringBuilder.append("#EXTINF:").append(String.format("%.3f", chunkDuration)).append(",\n");
                stringBuilder.append(fileName);
                stringBuilder.append("\n");
            }
        });

        stringBuilder.append("#EXT-X-ENDLIST");
        stringBuilder.append("\n");
        log.debug("done");
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

    public static InputStream writeMaster(StreamDataModel streamDataModel) {
        StringBuilder stringBuilder = new StringBuilder();
        log.debug("Writing playlist...");
        stringBuilder.append("#EXTM3U");
        for (String quality : streamDataModel.getQualities().keySet()) {
            stringBuilder.append(MasterPlaylistStrings.getStreamHeader(quality));
            stringBuilder.append('/').append(String.join("/",
                    streamDataModel.getStreamerName(),
                    streamDataModel.getUuid().toString(),
                    quality,
                    "index-dvr.m3u8")).append("\n");
        }
        log.debug("done");
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

}

