package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.StreamFileNameHelper;
import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.ChunkAppender;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.logging.Logger;

public class VodDownloader {
    private static Logger log = Logger.getLogger(VodDownloader.class.getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private ReadableByteChannel readableByteChannel;
    private LinkedHashSet<String> chunks = new LinkedHashSet<>();
    private String streamFilePath;

    public void initializeDownload(String recordingStreamName) {
        try {
            streamFilePath = SettingsProperties.getRecordedStreamPath()
                    + StreamFileNameHelper.makeFileName(recordingStreamName);
            Path streamFile = Paths.get(streamFilePath);
            Files.createDirectories(streamFile.getParent());
            Files.createFile(streamFile);
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link));
            StorageHelper.createChunksFolder(recordingStreamName);
            for (String chunkName : chunks) {
                this.downloadChunks(streamPath, chunkName);
            }
            this.recordCycle();

        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.severe("Vod downloader initialization failed" + e);
        }
    }

    private boolean refreshDownload() {
        boolean status = false;
        try {
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            BufferedReader reader = mediaPlaylistDownloader.getMediaPlaylist(m3u8Link);
            LinkedHashSet<String> refreshedPlaylist = MediaPlaylistParser.parse(reader);
            for (String chunkName : refreshedPlaylist) {
                status = chunks.add(chunkName);
                if (status) {
                    this.downloadChunks(streamPath, chunkName);
                }
            }
        } catch (IOException | URISyntaxException e) {
            log.severe("Vod downloader refresh failed." + e);
        }
        return status;
    }

    private void recordCycle() throws IOException, InterruptedException {
        while (VodIdGetter.getRecordStatus()) {
            this.refreshDownload();
            Thread.sleep(20 * 1000);
        }
        log.fine("Finalize record...");
        while (!this.refreshDownload()) {
            log.fine("Wait for renewing playlist");
            Thread.sleep(60 * 1000);
            log.fine("Try refresh playlist");
        }
        log.fine("End of list. Downloading last chunks");
        this.refreshDownload();
        log.info("Stop record");
        stopRecord();
    }

    private void downloadChunks(String streamPath, String chunkName) throws IOException {
        URL website = new URL(streamPath + "/" + chunkName);
        readableByteChannel = Channels.newChannel(website.openStream());
        InputStream inputStream = Channels.newInputStream(readableByteChannel);
        ChunkAppender.copyfile(streamFilePath, inputStream);
        inputStream.close();
        log.fine(chunkName + " complete");
    }

    private void stopRecord() {
        try {
            log.info("Closing vod downloader...");
            readableByteChannel.close();
            masterPlaylistDownloader.close();
            mediaPlaylistDownloader.close();
            if (SettingsProperties.getExecutePostDownloadCommand()) {
                PostDownloadHandler.handleDownloadedStream();
            }
        } catch (IOException e) {
            log.severe("VoD downloader record stop or uploading to GDrive failed" + e);
        }
    }
}
