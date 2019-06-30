package net.streamarchive.presentation.management.api;


import net.streamarchive.application.DateConverter;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.twitch.playlist.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.StreamNotFoundExeption;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.AddRequestModel;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamDocumentModel;
import net.streamarchive.repository.StatusRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * API for database and streams management
 * Endpoint {@code /database}
 */
@RestController
@RequestMapping("/api/v1/database")
public class StreamsApi {
    private final
    StatusRepository statusRepository;
    private final
    RecordStatusList recordStatusList;
    private final
    RecordThread vodRecorder;
    private final
    MongoTemplate mongoTemplate;
    private final
    VodMetadataHelper vodMetadataHelper;
    private final
    StorageHelper storageHelper;
    private final
    SettingsProperties settingsProperties;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public StreamsApi(StatusRepository statusRepository, RecordStatusList recordStatusList, RecordThread vodRecorder, MongoTemplate mongoTemplate, VodMetadataHelper vodMetadataHelper, StorageHelper storageHelper, SettingsProperties settingsProperties) {
        this.statusRepository = statusRepository;
        this.recordStatusList = recordStatusList;
        this.vodRecorder = vodRecorder;
        this.mongoTemplate = mongoTemplate;
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

    @RequestMapping(value = "/streams", method = RequestMethod.PUT)
    public void startRecord(AddRequestModel requestModel) {
        StreamDataModel streamMetadata = null;

        try {
            log.trace("type: {}", requestModel.getType());
            log.trace("value: {}", requestModel.getValue());
            if (requestModel.getType().equals("user")) {
                streamMetadata = vodMetadataHelper.getLastVod(requestModel.getValue());
            } else if (requestModel.getType().equals("vod")) {
                streamMetadata = vodMetadataHelper.getVodMetadata(Integer.valueOf(requestModel.getValue()));
            }
            if (streamMetadata != null) {
                //set another parent folder/db for stream ( for example if streamer is guest on another chanel)
                if (requestModel.getWriteTo() != null) {
                    streamMetadata.setUser(requestModel.getWriteTo());
                }

                if (streamMetadata.getVodId() != 0) {
                    //if uuid == null start new record, else start validation
                    if (requestModel.getUuid() != null) {
                        streamMetadata.setUuid(requestModel.getUuid());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.VALIDATION, DateConverter.convert(LocalDateTime.now()),
                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));
                        //if record exist in status DB then run validation
                    } else if (statusRepository.existsById(streamMetadata.getVodId())) {
                        streamMetadata.setUuid(statusRepository.findById(streamMetadata.getVodId()).get().getUuid());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.VALIDATION, DateConverter.convert(LocalDateTime.now()),
                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));
                    } else {
                        streamMetadata.setUuid(storageHelper.getUuidName());
                        recordStatusList.addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.MANUAL, DateConverter.convert(LocalDateTime.now()),
                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));
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
                throw new NotAcceptableExeption();
            }
        } catch (IOException | InterruptedException | StreamNotFoundExeption e) {
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
    @RequestMapping(value = "/streams/{user}/{uuid}", method = RequestMethod.DELETE)
    public void deleteStream(@PathVariable("uuid") String uuid, @PathVariable("user") String user, @RequestParam("deleteMedia") String deleteMedia) {
        if (Arrays.asList(settingsProperties.getUsers()).contains(user)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(uuid));

            if (mongoTemplate.remove(query, user).getDeletedCount() == 0L) {
                throw new NotModifiedException();
            } else {
                log.info("delete stream {}", uuid);
                if (deleteMedia.equals("true")) {
                    try {
                        FileUtils.deleteDirectory(new File(settingsProperties.getRecordedStreamPath() + "" + user + "/" + uuid));
                    } catch (IOException e) {
                        log.error("can't delete media {] ", e);
                        throw new NotModifiedException();
                    }
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
    @RequestMapping(value = "/streams/{user}/{uuid}", method = RequestMethod.PATCH)
    public void updateStream(@PathVariable("uuid") String uuid, @PathVariable("user") String user, StreamDataModel dataModel) {
        StreamDocumentModel streamDocumentModel = new StreamDocumentModel();
        streamDocumentModel.set_id(uuid);
        streamDocumentModel.setGame(dataModel.getGame());
        streamDocumentModel.setDate(dataModel.getDate());
        streamDocumentModel.setTitle(dataModel.getTitle());
        mongoTemplate.save(streamDocumentModel, user);
    }

    /**
     * Return streams array of selected streamer
     * Endpoint {@code /database/streams}
     *
     * @param user streamer name
     * @return array of streamer streams
     */
    @RequestMapping(value = "/streams/{user}", method = RequestMethod.GET)
    public List<StreamDocumentModel> getStreamsList(@PathVariable("user") String user) {
        log.trace(user);
        Query query = new Query();
        query.fields().include("title").include("date").include("game");
        if (Arrays.asList(settingsProperties.getUsers()).contains(user)) {
            return mongoTemplate.find(query, StreamDocumentModel.class, user);
        } else throw new NotFoundException();
    }

    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    private class NotFoundException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class NotModifiedException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    private class NotAcceptableExeption extends RuntimeException {
    }

}
