package com.pingwinno.domain;

import com.pingwinno.application.RecordTaskHandler;
import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.logging.Logger;

public class VodDownloader {
    private static Logger log = Logger.getLogger(VodDownloader.class.getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private ReadableByteChannel readableByteChannel;
    private LinkedHashSet<String> chunks = new LinkedHashSet<>();
    private String stringPath;
    private String vodId;
    private RecordTaskModel recordTask;

    public void initializeDownload(RecordTaskModel recordTask) {
        this.recordTask = recordTask;
        UUID uuid = recordTask.getUuid();
        stringPath = SettingsProperties.getRecordedStreamPath() + uuid.toString();

        try {
            RecordTaskHandler.saveTask(recordTask);
            try {
                Path streamPath = Paths.get(stringPath);
                Files.createDirectories(streamPath);

            } catch (IOException e) {
                log.severe("Can't create file or folder for VoD downloader" + e.toString());
            }
            vodId = recordTask.getVodId();

                String m3u8Link = MasterPlaylistParser.parse(
                        masterPlaylistDownloader.getPlaylist(vodId));
                //if stream  exist
                if (m3u8Link != null) {
                    String streamPath = StreamPathExtractor.extract(m3u8Link);
                    chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link));

                    for (String chunkName : chunks) {
                        this.downloadFile(streamPath, chunkName);
                    }
                    this.recordCycle();
                } else {
                    log.severe("vod id with id "+vodId+" not found. Close downloader thread...");
                    stopRecord();
                }

        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.severe("Vod downloader initialization failed" + e);
            stopRecord();
        }
    }

    private boolean refreshDownload() throws InterruptedException {
        boolean status = false;
        try {
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            BufferedReader reader = mediaPlaylistDownloader.getMediaPlaylist(m3u8Link);
            LinkedHashSet<String> refreshedPlaylist = MediaPlaylistParser.parse(reader);
            for (String chunkName : refreshedPlaylist) {
                status = chunks.add(chunkName);
                if (status) {
                    this.downloadFile(streamPath, chunkName);
                }
            }
        } catch (IOException | URISyntaxException e) {
            log.severe("Vod downloader refresh failed." + e);
            stopRecord();
        }
        return status;
    }

    private void recordCycle() throws IOException, InterruptedException, URISyntaxException {
        if (!RecordStatusGetter.getRecordStatus().equals("")) {
            while (RecordStatusGetter.getRecordStatus().equals("recording")) {
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
            this.downloadFile(StreamPathExtractor.extract(MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId))), "index-dvr.m3u8");

            log.info("Stop record");
            stopRecord();
        } else {
            log.severe("Getting status failed. Stoping cycle...");
            stopRecord();
        }
    }

    private void downloadFile(String streamPath, String fileName) throws IOException {
        URL website = new URL(streamPath + "/" + fileName);
        readableByteChannel = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(stringPath + "/" + fileName);
        fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fos.close();
        log.fine(fileName + " complete");
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
            RecordTaskHandler.removeTask(recordTask);
        } catch (IOException e) {
            log.severe("VoD downloader unexpectedly stop " + e);
        }
    }


}
