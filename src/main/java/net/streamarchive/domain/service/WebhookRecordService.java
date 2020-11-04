package net.streamarchive.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.twitch.handler.UserIdGetter;
import net.streamarchive.application.twitch.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.NotificationDataModel;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Service
public class WebhookRecordService {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    RecordStatusList recordStatusList;
    @Autowired
    VodMetadataHelper vodMetadataHelper;
    @Autowired
    SettingsProvider settingsProperties;
    @Autowired
    UserIdGetter userIdGetter;
    @Autowired
    StorageService dataHandler;
    @Autowired
    RecordThread recordThread;
    ObjectMapper objectMapper = new ObjectMapper();

    private Set<Integer> startedVods = new ConcurrentSkipListSet<>();


    public void handleLiveNotification(String user, String notification) throws JsonProcessingException, StreamNotFoundException {

        long userId = userIdGetter.getUserId(user);

        log.trace("User search res: {}", settingsProperties.isStreamerExist(user));
        if (!settingsProperties.isStreamerExist(user)) {
            log.warn("Subscription for {} canceled", user);
            return;
        }
        log.debug("Subscription is active");

        JsonNode notificationJson = objectMapper.readTree(notification).get("data");

        log.trace("JsonNode is: {}", notificationJson.asText());
        if (notificationJson.isEmpty()) {
            log.info("Stream down notification");
            return;
        }
        log.info("Stream is up");
        NotificationDataModel notificationModel = objectMapper.treeToValue(notificationJson.get(0), NotificationDataModel.class);

        if (!notificationModel.getType().equals("live")) {
            log.info("Stream isn't live stream. Ignoring...");
            return;
        }
        if (vodMetadataHelper.getLastVod(userId).getVodId() == 0) {
            log.error("vodId is null. Stream not found");
            return;
        }

        new Thread(() -> {
            long cycleStartTime = System.currentTimeMillis();

            try {
                Thread.sleep(10 * 1000);
                StreamDataModel streamMetadata = vodMetadataHelper.getLastVod(userId);
                if (statusRepository.existsById(streamMetadata.getVodId()) && startedVods.add(streamMetadata.getVodId())) {
                    log.warn("Stream duplicate. Skip...");
                    return;
                }

                streamMetadata.setUuid(dataHandler.getUUID());
                recordStatusList.addStatus
                        (new StatusDataModel(streamMetadata.getVodId(), streamMetadata.getUuid(),
                                StartedBy.WEBHOOK, Date.from(Instant.now()),
                                State.INITIALIZE, streamMetadata.getStreamerName()));
                int counter = 0;
                log.trace(streamMetadata.toString());
                while (!vodMetadataHelper.isRecording(streamMetadata.getVodId())) {
                    log.trace("vodId: {}", streamMetadata.getVodId());
                    streamMetadata = vodMetadataHelper.getLastVod(userId);
                    log.info("waiting for creating vod...");
                    Thread.sleep(20 * 1000);
                    long elapsedTime = (System.currentTimeMillis() - cycleStartTime) / 100;
                    log.warn("vod is not created yet... Elapsed {} seconds", counter);
                    counter++;
                    if (counter > 60) {
                        throw new StreamNotFoundException("new vod not found");
                    }
                }


                log.info("Try to start record");

                //check for notification duplicate
                log.info("check for duplicate notification");
                recordThread.start(streamMetadata);

            } catch (InterruptedException e) {
                log.error("DB error ", e);
            } catch (StreamNotFoundException streamNotFoundException) {
                streamNotFoundException.printStackTrace();
            }


        }).start();

        String startedAt = notificationModel.getStarted_at();
        log.info("Record started at: {}", startedAt);


    }
}
