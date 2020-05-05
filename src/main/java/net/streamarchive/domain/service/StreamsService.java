package net.streamarchive.domain.service;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.twitch.handler.UserIdGetter;
import net.streamarchive.application.twitch.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.exceptions.NotAcceptableException;
import net.streamarchive.infrastructure.exceptions.NotFoundException;
import net.streamarchive.infrastructure.exceptions.NotModifiedException;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.handlers.db.ArchiveDBHandler;
import net.streamarchive.infrastructure.models.*;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class StreamsService {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RecordStatusList recordStatusList;
    @Autowired
    private RecordThread vodRecorder;
    @Autowired
    private VodMetadataHelper vodMetadataHelper;
    @Autowired
    private StorageHelper storageHelper;
    @Autowired
    private SettingsProvider settingsProperties;
    @Autowired
    private ArchiveDBHandler archiveDBHandler;
    @Autowired
    private UserIdGetter userIdGetter;

    @Autowired
    private StorageService dataHandler;


    public void startRecord(AddRequestModel requestModel) {
        StreamDataModel streamMetadata = null;

        try {
            log.trace("type: {}", requestModel.getType());
            log.trace("value: {}", requestModel.getValue());
            if (requestModel.getType().equals("user")) {
                streamMetadata = vodMetadataHelper.getLastVod(userIdGetter.getUserId(requestModel.getValue()));
            } else if (requestModel.getType().equals("vod")) {
                streamMetadata = vodMetadataHelper.getVodMetadata(Integer.parseInt(requestModel.getValue()));
            }
            if (streamMetadata == null) {
                throw new NotAcceptableException();
            }
            //set another parent folder/db for stream ( for example if streamer is guest on another chanel)
            if (requestModel.getWriteTo() != null) {
                streamMetadata.setStreamerName(requestModel.getWriteTo());
            }

            if (streamMetadata.getVodId() == 0) {
                throw new NotFoundException();
            }
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


        } catch (StreamNotFoundException e) {
            log.error("Can't start record ", e);
            throw new NotFoundException();
        }
    }

    public void updateStream(UUID uuid, String streamer, StreamDataModel dataModel) {
        try {
            Stream stream = archiveDBHandler.getStream(streamer, uuid);
            stream.setGame(dataModel.getGame());
            stream.setDate(dataModel.getDate());
            stream.setTitle(dataModel.getTitle());
            archiveDBHandler.updateStream(stream);
        } catch (StreamerNotFoundException | StreamNotFoundException e) {
            log.error("Stream not found", e);
            throw new NotFoundException();
        }
    }

    public void deleteStream(UUID uuid, String streamer, String deleteMedia) {
        if (settingsProperties.isStreamerExist(streamer)) {
            try {
                archiveDBHandler.deleteStream(archiveDBHandler.getStream(streamer, uuid));
            } catch (StreamNotFoundException e) {
                log.error("Stream not found");
                throw new NotModifiedException();
            }
            log.info("delete stream {}", uuid);
            if (deleteMedia.equals("true")) {
                dataHandler.deleteStream(uuid, streamer);
            }
        } else {
            throw new NotFoundException();
        }
    }

    public List<Stream> getStreams(String streamer) {
        try {
            return archiveDBHandler.getAllStreams(streamer);
        } catch (StreamerNotFoundException e) {
            throw new NotFoundException();
        }
    }
}
