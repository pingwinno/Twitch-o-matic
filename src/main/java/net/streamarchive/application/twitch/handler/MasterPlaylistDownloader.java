package net.streamarchive.application.twitch.handler;


import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamFileModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class MasterPlaylistDownloader {

    public static Map<String, Set<StreamFileModel>> getPlaylist(StreamDataModel streamDataModel) throws IOException, StreamNotFoundException, InterruptedException {
        Map<String, Set<StreamFileModel>> playlists = new HashMap<>();
        var baseUrl = streamDataModel.getBaseUrl();
        for (String quality : streamDataModel.getQualities().keySet()) {
            String streamLink = baseUrl + quality + "/index-dvr.m3u8";
            String basePath = String.join("/", streamDataModel.getStreamerName(), streamDataModel.getUuid().toString(), quality) ;
            playlists.put(quality,MediaPlaylistParser.getChunks(MediaPlaylistDownloader.getMediaPlaylist(streamLink), baseUrl + quality, basePath, streamDataModel.isSkipMuted()));
            log.trace("Stream link is: {}", streamLink);
        }
        return playlists;
    }
}
