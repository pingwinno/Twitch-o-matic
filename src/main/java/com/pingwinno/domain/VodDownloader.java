package com.pingwinno.domain;

import com.pingwinno.application.RecordTaskHandler;
import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VodDownloader {
    private static Logger log = Logger.getLogger(VodDownloader.class.getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private ReadableByteChannel readableByteChannel;
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
            if (RecordStatusGetter.getRecordStatus(vodId).equals("recording")){
                threadsNumber = 2;
            }
            else{
                threadsNumber =10;
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
                    log.warning("Stream folder exist. Maybe it's unfinished task. " +
                            "If task can't be complete, it will be remove from task list.");
                    log.info("Trying finish download...");
                }
            } catch (IOException e) {
                log.severe("Can't create file or folder for VoD downloader" + e.toString());
            }
            vodId = streamDataModel.getVodId();

            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId));
            //if stream  exist
            if (m3u8Link != null) {
                String streamPath = StreamPathExtractor.extract(m3u8Link);
                chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link));
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
                log.severe("vod id with id " + vodId + " not found. Close downloader thread...");
                stopRecord();
            }
            this.recordCycle();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.severe("Vod downloader initialization failed" + e);
            stopRecord();
        }
    }

   synchronized private boolean refreshDownload() throws InterruptedException {
        boolean status = false;
        try {
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            BufferedReader reader = mediaPlaylistDownloader.getMediaPlaylist(m3u8Link);
            LinkedHashSet<String> refreshedPlaylist = MediaPlaylistParser.parse(reader);

            ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
            for (String chunkName : refreshedPlaylist) {

                status = chunks.add(chunkName);
                if (status) {
                    Runnable runnable = () -> {
                        try {
                            downloadChunk(streamPath, chunkName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    executorService.execute(runnable);
                }

            }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (IOException | URISyntaxException e) {
            log.severe("Vod downloader refresh failed." + e);
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

            downloadFile(streamDataModel.getPreviewUrl(),"preview.jpg");
            downloadFile(MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId)),"index-dvr.m3u8");
            DataBaseHandler dataBaseHandler = new DataBaseHandler(streamDataModel);
            log.info("write to local db");
            dataBaseHandler.writeToLocalDB();
            log.info("write to remote db");
            dataBaseHandler.writeToRemoteDB();

            log.info("Stop record");
            stopRecord();
        } else {
            log.severe("Getting status failed. Stop cycle...");
            stopRecord();
        }
    }

    private void downloadChunk(String streamPath, String fileName) throws IOException {
        URL website = new URL(streamPath + "/" + fileName);
        URLConnection connection = website.openConnection();
        if ((!Files.exists(Paths.get(streamFolderPath + "/" + fileName))) ||
        (connection.getContentLengthLong() != Files.size((Paths.get(streamFolderPath + "/" + fileName))))){

            readableByteChannel = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(streamFolderPath + "/" + fileName);
            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fos.close();
            log.info(fileName + " complete");
        }else {
            log.info("Chunk exist. Skipping...");
        }
    }

    private void downloadFile(String url, String fileName) throws IOException {
        readableByteChannel = Channels.newChannel(new URL(url).openStream());
        FileOutputStream fos = new FileOutputStream(streamFolderPath + "/" + fileName);
        fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fos.close();
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
            RecordTaskHandler.removeTask(streamDataModel);
        } catch (IOException e) {
            log.severe("VoD downloader unexpectedly stop " + e);
        }
    }
    private void stopRecord(boolean flag){
        if (flag){
            stopRecord();
        }
    }


}
