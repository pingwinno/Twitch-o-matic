package net.streamarchive.presentation.management.api;


import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.twitch.handler.UserIdGetter;
import net.streamarchive.application.twitch.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.handlers.db.ArchiveDBHandler;
import net.streamarchive.infrastructure.models.*;
import net.streamarchive.repository.StatusRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * API for database and streams management
 * Endpoint {@code /database}
 */
@RestController
@RequestMapping("/api/v1/database/streams")
public class StreamsApi {
    private final
    StatusRepository statusRepository;
    private final
    RecordStatusList recordStatusList;
    private final
    RecordThread vodRecorder;

    private final
    VodMetadataHelper vodMetadataHelper;
    private final
    StorageHelper storageHelper;
    private final
    SettingsProperties settingsProperties;
    @Autowired
    ArchiveDBHandler archiveDBHandler;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public StreamsApi(StatusRepository statusRepository, RecordStatusList recordStatusList, RecordThread vodRecorder, VodMetadataHelper vodMetadataHelper, StorageHelper storageHelper, SettingsProperties settingsProperties) {
        this.statusRepository = statusRepository;
        this.recordStatusList = recordStatusList;
        this.vodRecorder = vodRecorder;
        this.vodMetadataHelper = vodMetadataHelper;
        this.storageHelper = storageHelper;
        this.settingsProperties = settingsProperties;
    }

    /**
     * Start recording new stream
     * Endpoint {@code /database/add}
     *
     * @param requestModel request params
     * @see AddRequestModel for review json fields
     */

    @RequestMapping(method = RequestMethod.PUT)
    public void startRecord(@RequestBody AddRequestModel requestModel) {
        StreamDataModel streamMetadata = null;

        try {
            log.trace("type: {}", requestModel.getType());
            log.trace("value: {}", requestModel.getValue());
            if (requestModel.getType().equals("user")) {
                streamMetadata = vodMetadataHelper.getLastVod(UserIdGetter.getUserId(requestModel.getValue()));
            } else if (requestModel.getType().equals("vod")) {
                streamMetadata = vodMetadataHelper.getVodMetadata(Integer.parseInt(requestModel.getValue()));
            }
            if (streamMetadata != null) {
                //set another parent folder/db for stream ( for example if streamer is guest on another chanel)
                if (requestModel.getWriteTo() != null) {
                    streamMetadata.setStreamerName(requestModel.getWriteTo());
                }

                if (streamMetadata.getVodId() != 0) {
                    //if uuid == null start new record, else start validation
                    if (requestModel.getUuid() != null) {
                        streamMetadata.setUuid(requestModel.getUuid());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), streamMetadata.getUuid(), StartedBy.VALIDATION, Date.from(Instant.now()),
                                        State.INITIALIZE, streamMetadata.getStreamerName()));
                        //if record exist in status DB then run validation
                    } else if (statusRepository.existsById(streamMetadata.getVodId())) {
                        streamMetadata.setUuid(statusRepository.findById(streamMetadata.getVodId()).get().getUuid());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), streamMetadata.getUuid(), StartedBy.VALIDATION, Date.from(Instant.now()),
                                        State.INITIALIZE, streamMetadata.getStreamerName()));
                    } else {
                        streamMetadata.setUuid(storageHelper.getUuidName());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), streamMetadata.getUuid(), StartedBy.MANUAL, Date.from(Instant.now()),
                                        State.INITIALIZE, streamMetadata.getStreamerName()));
                    }

                    streamMetadata.setSkipMuted(requestModel.isSkipMuted());
                    StreamDataModel finalStreamMetadata = streamMetadata;

                    new Thread(() -> vodRecorder.start(finalStreamMetadata)).start();

                    Date startedAt = streamMetadata.getDate();
                    log.info("Record started at:{} ", startedAt);

                } else {
                    log.error("Stream {} not found", requestModel.getValue());
                    throw new NotFoundException();
                }
            } else {
                throw new NotAcceptableException();
            }
        } catch (IOException | InterruptedException | StreamNotFoundException e) {
            log.error("Can't start record ", e);
            throw new NotFoundException();
        }
    }

    /**
     * Delete stream from database
     * Endpoint {@code /database/{user}/{uuid}/}
     *
     * @param uuid        UUID of stream
     * @param user        name of streamer
     * @param deleteMedia if "true" also delete stream from storage
     * @throws NotModifiedException if can't delete stream from storage (right issue/etc)
     */
    @RequestMapping(value = "{user}/{uuid}", method = RequestMethod.DELETE)
    public void deleteStream(@PathVariable("uuid") String uuid, @PathVariable("user") String user, @RequestParam("deleteMedia") String deleteMedia) {
        if (settingsProperties.isUserExist(user)) {
            try {
                archiveDBHandler.deleteStream(archiveDBHandler.getStream(user, UUID.fromString(uuid)));
            } catch (StreamNotFoundException e) {
                log.error("Stream not found");
                throw new NotModifiedException();
            }
            log.info("delete stream {}", uuid);
            if (deleteMedia.equals("true")) {
                try {
                    FileUtils.deleteDirectory(new File(settingsProperties.getRecordedStreamPath() + "" + user + "/" + uuid));
                } catch (IOException e) {
                    log.error("can't delete media {] ", e);
                    throw new NotModifiedException();
                }
            }
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * Update database record
     * Endpoint {@code /database/{user}/{uuid}/}
     *
     * @param user      name of streamer
     * @param uuid      UUID of stream
     * @param dataModel updated metadata
     * @see StreamDataModel for see required fields
     */
    @RequestMapping(value = "/{user}/{uuid}", method = RequestMethod.PATCH)
    public void updateStream(@PathVariable("uuid") String uuid, @PathVariable("user") String user, @RequestBody StreamDataModel dataModel) {
        try {
            Stream stream = archiveDBHandler.getStream(user, UUID.fromString(uuid));
            stream.setGame(dataModel.getGame());
            stream.setDate(dataModel.getDate());
            stream.setTitle(dataModel.getTitle());
            archiveDBHandler.updateStream(stream);
        } catch (StreamerNotFoundException | StreamNotFoundException e) {
            log.error("Stream not found", e);
            throw new NotFoundException();
        }

    }

    /**
     * Return streams array of selected streamer
     * Endpoint {@code /database/streams}
     *
     * @param user streamer name
     * @return array of streamer streams
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public List<Stream> getStreamsList(@PathVariable("user") String user) {
        try {
            return archiveDBHandler.getAllStreams(user);
        } catch (StreamerNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    private static class NotModifiedException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class NotFoundException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    private static class NotAcceptableException extends RuntimeException {
    }

}
