package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.StreamFileNameHelper;
import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.ChunkAppender;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

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
import java.util.LinkedList;
import java.util.logging.Logger;

public class VodDownloader {
    private static Logger log = Logger.getLogger(VodDownloader.class.getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private ReadableByteChannel rbc;
    private FileOutputStream fos;
    private LinkedHashSet<String> chunks = new LinkedHashSet<>();
    private LinkedList<String> downloadedChunks = new LinkedList<>();
    private String recordingStreamName;
    private String streamFileName;

    public void initializeDownload(String recordingStreamName) {
            this.recordingStreamName = recordingStreamName;
        try {
            streamFileName = SettingsProperties.getRecordedStreamPath()
                    + StreamFileNameHelper.makeFileName(recordingStreamName);
            Path streamFile = Paths.get(streamFileName);
            Files.createDirectories(streamFile.getParent());
            Files.createFile(streamFile);
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            System.out.println(streamPath);
            chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link));
            StorageHelper.createChunksFolder(recordingStreamName);
            for (String chunkName : chunks) {
              this.downloadChunks(streamPath,chunkName);
              }
            this.compileChunks();
            this.recordCycle();

        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean refreshDownload(){
        boolean status = false;
        try {
            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            System.out.println(streamPath);
            BufferedReader reader = mediaPlaylistDownloader.getMediaPlaylist(m3u8Link);
            LinkedHashSet<String> refreshedPlaylist = MediaPlaylistParser.parse(reader);

            for (String chunkName : refreshedPlaylist) {
                status = chunks.add(chunkName);
                if (status) {
                    this.downloadChunks(streamPath,chunkName);
                }
            }
            System.out.println(chunks);
            this.compileChunks();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return status;
    }


    private void compileChunks() {
        while (downloadedChunks.iterator().hasNext()) {

            ChunkAppender.copyfile(streamFileName, downloadedChunks.poll());
        }
    }


    public void stopRecord() {
        try {
            log.info("Closing vod downloader...");
            fos.close();
            rbc.close();
            masterPlaylistDownloader.close();
            mediaPlaylistDownloader.close();
            GoogleDriveService.upload(streamFileName, StreamFileNameHelper.makeFileName(recordingStreamName));
            log.info("Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void recordCycle() throws IOException, InterruptedException {
        int counter = 0;
        while (VodIdGetter.getRecordStatus()) {
            this.refreshDownload();
            log.info("Cycle: " + counter);
            counter++;
            Thread.sleep(20 * 1000);
        }
        log.info("Finalize record...");
        while (!this.refreshDownload()) {
            log.info("Wait for renewing playlist");
            Thread.sleep(60 * 1000);
            log.info("Try refresh playlist");
        }
        log.info("End of list. Downloading last chunks");
        this.refreshDownload();
        log.info("Stop record");
        stopRecord();
    }

    private void downloadChunks(String streamPath, String chunkName) throws IOException {
        String chunkFile = StreamFileNameHelper.makeStreamFolderPath(recordingStreamName) + "/" + chunkName;
        URL website = new URL(streamPath + "/" + chunkName);
        rbc = Channels.newChannel(website.openStream());
        fos = new FileOutputStream(chunkFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        downloadedChunks.add(chunkFile);
        log.info(chunkName + " complete");
    }
}
