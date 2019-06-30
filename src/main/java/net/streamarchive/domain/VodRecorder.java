package net.streamarchive.domain;

import net.streamarchive.application.AnimatedPreviewGenerator;
import net.streamarchive.application.FrameGrabber;
import net.streamarchive.application.TimelinePreviewGenerator;
import net.streamarchive.application.twitch.playlist.handler.*;
import net.streamarchive.infrastructure.*;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.Preview;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamDocumentModel;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Service
@Scope("prototype")
public class VodRecorder implements RecordThread {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private final MasterPlaylistDownloader masterPlaylistDownloader;

    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private LinkedHashMap<String, Double> mainPlaylist;
    private String streamFolderPath;
    private int vodId;
    private StreamDataModel streamDataModel;
    private int threadsNumber = 1;
    private UUID uuid;
    private ExecutorService executorService;
    private Thread thisTread = Thread.currentThread();
    private boolean isRecordTerminated;
    private StreamDocumentModel streamDocumentModel = new StreamDocumentModel();

    private final
    RecordStatusList recordStatusList;

    private final
    RecordThreadSupervisor recordThreadSupervisor;
    private final
    DataBaseWriter dataBaseWriter;
    private final
    SettingsProperties settingsProperties;
    private final
    RecordStatusGetter recordStatusGetter;
    private final
    VodMetadataHelper vodMetadataHelper;
    private final
    AnimatedPreviewGenerator animatedPreviewGenerator;
    private final
    TimelinePreviewGenerator timelinePreviewGenerator;

    public VodRecorder(RecordStatusList recordStatusList, MasterPlaylistDownloader masterPlaylistDownloader, RecordThreadSupervisor recordThreadSupervisor, DataBaseWriter dataBaseWriter, SettingsProperties settingsProperties, RecordStatusGetter recordStatusGetter, VodMetadataHelper vodMetadataHelper, AnimatedPreviewGenerator animatedPreviewGenerator, TimelinePreviewGenerator timelinePreviewGenerator) {
        this.recordStatusList = recordStatusList;
        this.masterPlaylistDownloader = masterPlaylistDownloader;
        this.recordThreadSupervisor = recordThreadSupervisor;
        this.dataBaseWriter = dataBaseWriter;
        this.settingsProperties = settingsProperties;
        this.recordStatusGetter = recordStatusGetter;
        this.vodMetadataHelper = vodMetadataHelper;
        this.animatedPreviewGenerator = animatedPreviewGenerator;
        this.timelinePreviewGenerator = timelinePreviewGenerator;
    }

    public void start(StreamDataModel streamDataModel) {

        this.streamDataModel = streamDataModel;
        uuid = streamDataModel.getUuid();
        streamFolderPath = settingsProperties.getRecordedStreamPath() + streamDataModel.getUser() + "/" + uuid.toString();
        vodId = streamDataModel.getVodId();
        recordThreadSupervisor.add(uuid, this);
        try {
            if (recordStatusGetter.isRecording(vodId)) {
                threadsNumber = 2;
                log.info("Wait for creating vod...");
            } else {
                threadsNumber = 10;
            }
        } catch (IOException | InterruptedException e) {
            log.error("Can't start record. ", e);
            recordStatusList.changeState(uuid, State.ERROR);
        }
        try {
            recordStatusList.changeState(uuid, State.RUNNING);
            streamDocumentModel.set_id(streamDataModel.getUuid().toString());
            streamDocumentModel.setTitle(streamDataModel.getTitle());
            streamDocumentModel.setDate(streamDataModel.getDate());
            streamDocumentModel.setGame(streamDataModel.getGame());

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
                recordStatusList.changeState(uuid, State.ERROR);
                log.error("Can't create file or folder for VoD downloader. ", e);
            }
            dataBaseWriter.writeToRemoteDB(streamDocumentModel, streamDataModel.getUser());
            vodId = streamDataModel.getVodId();

            String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(vodId), settingsProperties.getStreamQuality());
            //if stream  exist
            if (m3u8Link != null) {
                String streamPath = StreamPathExtractor.extract(m3u8Link);
                mainPlaylist = MediaPlaylistParser.getChunks(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link), streamDataModel.isSkipMuted());
                executorService = Executors.newFixedThreadPool(threadsNumber);

                for (Map.Entry<String, Double> chunk : mainPlaylist.entrySet()) {
                    String chunkName = chunk.getKey();
                    Runnable runnable = () -> {
                        if (!isRecordTerminated) {
                            downloadChunk(streamPath, chunkName);
                        }
                    };
                    executorService.execute(runnable);
                }
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } else {
                recordStatusList.changeState(uuid, State.ERROR);
                log.error("vod id with id {} not found. Close downloader thread...", vodId);
                stop();
            }
            this.recordCycle();
        } catch (IOException | URISyntaxException | InterruptedException | StreamNotFoundExeption e) {
            log.error("Vod downloader initialization failed. ", e);
            recordStatusList.changeState(uuid, State.ERROR);
            stop();
        }
    }

    private boolean refreshDownload() throws InterruptedException {
        boolean status = false;
        try {
            String m3u8Link = null;
            int counter = 0;
            //TODO Find out what the problem is
            do {
                try {
                    m3u8Link = MasterPlaylistParser.parse(
                            masterPlaylistDownloader.getPlaylist(vodId), settingsProperties.getStreamQuality());
                } catch (UnknownHostException e) {
                    counter++;
                    log.warn("UnknownHostException. cycle:{} \n Stacktrace{}", counter, e);
                }

            } while (m3u8Link == null && counter < 10);

            if (m3u8Link != null) {
                String streamPath = StreamPathExtractor.extract(m3u8Link);

                LinkedHashMap<String, Double> refreshedPlaylist =
                        MediaPlaylistParser.getChunks(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link), streamDataModel.isSkipMuted());

                executorService = Executors.newFixedThreadPool(threadsNumber);

                status = !mainPlaylist.equals(refreshedPlaylist);
                log.trace("Renew playlist status: {}", status);
                if (status) {
                    refreshedPlaylist.forEach((chunkName, time) -> {

                        if (!mainPlaylist.containsKey(chunkName)) {
                            log.trace("chunk: {}", chunkName);
                            Runnable runnable = () -> {
                                if (!isRecordTerminated)
                                    downloadChunk(streamPath, chunkName);

                            };
                            executorService.execute(runnable);
                        }
                    });


                    executorService.shutdown();
                    executorService.awaitTermination(10, TimeUnit.MINUTES);
                    mainPlaylist.clear();
                    mainPlaylist.putAll(refreshedPlaylist);
                }
            } else {
                log.warn("UnknownHostException again. Why? I don't give a fuck.");
            }
        } catch (IOException | URISyntaxException | StreamNotFoundExeption e) {
            log.error("Vod downloader refresh failed. ", e);
            recordStatusList.changeState(uuid, State.ERROR);
            stop();
        }
        return status;
    }

    private void recordCycle() throws IOException, InterruptedException {

        while (recordStatusGetter.isRecording(vodId)) {
            refreshDownload();
            Thread.sleep(20 * 1000);
        }
        log.info("Finalize record...");
        int counter = 0;
        while ((!this.refreshDownload()) && (counter <= 10)) {
            log.info("Wait for renewing playlist");
            Thread.sleep(10 * 1000);
            counter++;
        }
        Thread.sleep(100 * 1000);
        refreshDownload();
        log.info("End of list. Downloading last mainPlaylist");
        log.debug("Download preview");
        try {
            downloadPreview(vodMetadataHelper.getVodMetadata(streamDataModel.getVodId()).getPreviewUrl());

        } catch (StreamNotFoundExeption e) {
            int streamLength = mainPlaylist.size() / 2;
            ImageIO.write(FrameGrabber.getFrame(streamFolderPath + "/" + streamLength + ".ts", 640, 360),
                    "jpeg", new File(streamFolderPath + "/preview.jpg"));

        }
        log.debug("Download m3u8");
        MediaPlaylistWriter.write(mainPlaylist, streamFolderPath);

        LinkedHashMap<String, String> animatedPreview = animatedPreviewGenerator.generate(streamDataModel, mainPlaylist);
        LinkedHashMap<String, Preview> timelinePreview = timelinePreviewGenerator.generate(streamDataModel, mainPlaylist);


        streamDocumentModel.setDuration(mainPlaylist.size() * 10);
        streamDocumentModel.setAnimatedPreviews(animatedPreview);
        streamDocumentModel.setTimeline_preview(timelinePreview);
        dataBaseWriter.writeToRemoteDB(streamDocumentModel, streamDataModel.getUser());
        recordStatusList.changeState(uuid, State.COMPLETE);
        stop();
    }


    private void downloadChunk(String streamPath, String fileName) {
        URL website;
        URLConnection connection;

        try {
            website = new URL(streamPath + "/" + fileName);

            if (fileName.contains("muted")) {
                fileName = fileName.replace("-muted", "");
            }

            connection = website.openConnection();

            if ((!Files.exists(Paths.get(streamFolderPath + "/" + fileName))) ||
                    (connection.getContentLengthLong() > Files.size((Paths.get(streamFolderPath + "/" + fileName))))) {

                try (InputStream in = website.openStream()) {

                    Files.copy(in, Paths.get(streamFolderPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                    if (Integer.valueOf(fileName.replaceAll(".ts", "")) % 10 == 0) {
                        log.info(fileName + " complete");
                    }
                    log.trace(fileName + " complete");
                }
            } else {
                log.trace("Chunk {} exist. Skipping...",fileName);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("Download failed");
        }
    }

    private void downloadPreview(String url) throws IOException {

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(streamFolderPath + "/" + "preview.jpg"), StandardCopyOption.REPLACE_EXISTING);
            log.info("preview.jpg" + " complete");
        }
    }

    public void stop() {
        try {
            log.info("Stop record");
            log.info("Closing vod downloader...");
            if (!executorService.isShutdown()) {
                executorService.shutdownNow();
            }
            log.debug(
                    "Downloader pool stopped: {}", executorService.isShutdown());
            masterPlaylistDownloader.close();
            isRecordTerminated = true;
            thisTread.interrupt();

        } catch (IOException e) {
            recordStatusList.changeState(uuid, State.ERROR);
            log.error("VoD downloader unexpectedly stop.", e);
        }
    }

}
