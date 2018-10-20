package com.pingwinno.domain;

import com.pingwinno.application.RecordTaskHandler;
import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VodDownloader {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private LinkedHashSet<String> chunks = new LinkedHashSet<>();
    private String streamFolderPath;
    private String vodId;
    private StreamExtendedDataModel streamDataModel;
    private int threadsNumber = 1;

    public void initializeDownload(StreamExtendedDataModel streamDataModel) {

        this.streamDataModel = streamDataModel;
        UUID uuid = streamDataModel.getUuid();
        streamFolderPath = SettingsProperties.getRecordedStreamPath() + uuid.toString();
        vodId = streamDataModel.getVodId();

        try {
            if (RecordStatusGetter.getRecordStatus(vodId).equals("recording")) {
                threadsNumber = 2;
            } else {
                threadsNumber = 10;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            RecordTaskHandler.saveTask(streamDataModel);
            try {
                Path streamPath = Paths.get(streamFolderPath);
                if (!Files.exists(streamPath)) {
                    Files.createDirectories(streamPath);
                } else {
                    log.warn("Stream folder exist. Maybe it's unfinished task. " +
                            "If task can't be complete, it will be remove from task list.");
                    log.info("Trying finish download...");
                }
            } catch (IOException e) {
                log.error("Can't create file or folder for VoD downloader. {}", e);
            }
            vodId = streamDataModel.getVodId();

            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId));
            //if stream  exist
            if (m3u8Link != null) {
                String streamPath = StreamPathExtractor.extract(m3u8Link);
                chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link), true);
                ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);

                for (String chunkName : chunks) {
                    Runnable runnable = () -> {
                        try {
                            downloadChunk(streamPath, chunkName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    executorService.execute(runnable);
                }
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } else {
                log.error("vod id with id {} not found. Close downloader thread...", vodId);
                stopRecord();
            }
            this.recordCycle();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.error("Vod downloader initialization failed. {}", e);
            stopRecord();
        }
    }

    synchronized private boolean refreshDownload() throws InterruptedException {
        boolean status = false;
        try {
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId));
            String streamPath = StreamPathExtractor.extract(m3u8Link);

            LinkedHashSet<String> refreshedPlaylist =
                    MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link), true);

            ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
            for (String chunkName : refreshedPlaylist) {

                status = chunks.add(chunkName);
                if (status) {
                    Runnable runnable = () -> {
                        try {
                            downloadChunk(streamPath, chunkName);
                        } catch (IOException e) {
                            log.error("Chunk download error. {}", e);
                        }
                    };
                    executorService.execute(runnable);
                }

            }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (IOException | URISyntaxException e) {
            log.error("Vod downloader refresh failed. {}", e);
            stopRecord();
        }
        return status;
    }

    private void recordCycle() throws IOException, InterruptedException, URISyntaxException {
        if (!RecordStatusGetter.getRecordStatus(vodId).equals("")) {
            while (RecordStatusGetter.getRecordStatus(vodId).equals("recording")) {
                refreshDownload();
                Thread.sleep(20 * 1000);
            }
            log.info("Finalize record...");
            log.info("Wait for renewing playlist");
            //Thread.sleep(100 * 1000);
            log.info("End of list. Downloading last chunks");
            this.refreshDownload();

            log.debug("Download preview");
            downloadFile(streamDataModel.getPreviewUrl(), "preview.jpg");
            log.debug("Download m3u8");
            MediaPlaylistWriter.write( new MediaPlaylistDownloader().getMediaPlaylist(MasterPlaylistParser.parse
                    (new MasterPlaylistDownloader().getPlaylist(vodId))), streamFolderPath);
            DataBaseHandler dataBaseHandler = new DataBaseHandler(streamDataModel);
            log.debug("write to local db");
            dataBaseHandler.writeToLocalDB();
            log.debug("write to remote db");
            dataBaseHandler.writeToRemoteDB();

            stopRecord();
        } else {
            log.error("Getting status failed. Stop cycle...");
            stopRecord();
        }
    }

    private void downloadChunk(String streamPath, String fileName) throws IOException {
        URL website = new URL(streamPath + "/" + fileName);
        URLConnection connection = website.openConnection();
        if ((!Files.exists(Paths.get(streamFolderPath + "/" + fileName))) ||
                (connection.getContentLengthLong() != Files.size((Paths.get(streamFolderPath + "/" + fileName))))) {

            try (InputStream in = website.openStream()) {
                Files.copy(in, Paths.get(streamFolderPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                log.info(fileName + " complete");
            }
        } else {
            log.info("Chunk exist. Skipping...");
        }
    }

    private void downloadFile(String url, String fileName) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(streamFolderPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
            log.info(fileName + " complete");
        }
    }

    private void stopRecord() {
        try {
            log.info("Stop record");
            log.info("Closing vod downloader...");
            masterPlaylistDownloader.close();
            mediaPlaylistDownloader.close();
            if (SettingsProperties.getExecutePostDownloadCommand()) {
                PostDownloadHandler.handleDownloadedStream();
            }
            RecordTaskHandler.removeTask(streamDataModel);
        } catch (IOException e) {
            log.error("VoD downloader unexpectedly stop. {}", e);
        }
    }

    private void stopRecord(boolean flag) {
        if (flag) {
            stopRecord();
        }
    }


}
