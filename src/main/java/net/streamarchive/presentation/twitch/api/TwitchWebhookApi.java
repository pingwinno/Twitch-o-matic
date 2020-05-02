package net.streamarchive.presentation.twitch.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.twitch.handler.UserIdGetter;
import net.streamarchive.application.twitch.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.handlers.misc.HashHandler;
import net.streamarchive.infrastructure.models.NotificationDataModel;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamStatusNotificationModel;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/handler")
public class TwitchWebhookApi implements ApplicationContextAware {
    private final
    StatusRepository statusRepository;
    private final
    RecordStatusList recordStatusList;
    private final
    HashHandler hashHandler;
    private final
    VodMetadataHelper vodMetadataHelper;
    private final
    SettingsProvider settingsProperties;
    private final
    StorageHelper storageHelper;
    private ApplicationContext applicationContext;

    @Autowired
    private UserIdGetter userIdGetter;


    @Autowired
    public TwitchWebhookApi(StatusRepository statusRepository, RecordStatusList recordStatusList, VodMetadataHelper vodMetadataHelper, HashHandler hashHandler, SettingsProvider settingsProperties, StorageHelper storageHelper) {
        this.statusRepository = statusRepository;
        this.recordStatusList = recordStatusList;
        this.hashHandler = hashHandler;
        this.vodMetadataHelper = vodMetadataHelper;
        this.settingsProperties = settingsProperties;
        this.storageHelper = storageHelper;
    }

    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public String getSubscriptionQuery(@RequestParam Map<String, String> allParams, @PathVariable("user") String user) {

        log.debug("Incoming challenge request");
        if (allParams != null) {
            log.debug("hub.mode {} ", allParams.get("hub.mode"));
            String hubMode = allParams.get("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = allParams.get("hub.reason");

                log.warn("Subscription failed. Reason:{}", hubReason);
                return null;
            }
            //handle verify response
            else {
                log.debug("Subscription complete {} hub.challenge is:", allParams.get("hub.challenge"));
                log.info(" Twith-o-matic started for {}. Waiting for stream up", user);
                return allParams.get("hub.challenge");

            }
        } else {
            log.warn("Subscription query is not correct. Try restart Twitch-o-matic.");
        }

        return null;
    }

    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public void handleStreamNotification(@RequestBody String stringDataModel
            , @RequestHeader("content-length") long length, @RequestHeader("X-Hub-Signature") String signature, @PathVariable("user") String user) throws InterruptedException, StreamNotFoundException, IOException {

        long userId = userIdGetter.getUserId(user);
        log.debug("Incoming stream up/down notification");

        log.debug("data length {} body length {} ", stringDataModel.length(), length);
        log.debug(stringDataModel);
        if (hashHandler.compare(signature, stringDataModel)) {
            log.debug("Hash confirmed");
            //check for active subscription
            log.trace("User search res: {}", settingsProperties.isStreamerExist(user));
            if (settingsProperties.isStreamerExist(user)) {
                log.debug("Subscription is active");
                StreamStatusNotificationModel dataModel =
                        new ObjectMapper().readValue(stringDataModel, StreamStatusNotificationModel.class);
                NotificationDataModel[] notificationArray = dataModel.getData();
                if (notificationArray.length > 0) {
                    log.info("Stream is up");
                    NotificationDataModel notificationModel = notificationArray[0];


                    if (notificationModel.getType().equals("live")) {

                        if (vodMetadataHelper.getLastVod(userId).getVodId() != 0) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(10 * 1000);
                                    StreamDataModel streamMetadata = vodMetadataHelper.getLastVod(userId);
                                    int counter = 0;
                                    log.trace(streamMetadata.toString());
                                    while (!vodMetadataHelper.isRecording(streamMetadata.getVodId())) {
                                        log.trace("vodId: {}", streamMetadata.getVodId());
                                        streamMetadata = vodMetadataHelper.getLastVod(userId);
                                        log.info("waiting for creating vod...");
                                        Thread.sleep(20 * 1000);
                                        log.warn("vod is not created yet... cycle " + counter);
                                        counter++;
                                        if (counter > 60) {
                                            throw new StreamNotFoundException("new vod not found");
                                        }
                                    }


                                    if (!statusRepository.existsById(streamMetadata.getVodId())) {
                                        streamMetadata.setUuid(storageHelper.getUuidName());

                                        recordStatusList.addStatus
                                                (new StatusDataModel(streamMetadata.getVodId(), streamMetadata.getUuid(),
                                                        StartedBy.WEBHOOK, Date.from(Instant.now()),
                                                        State.INITIALIZE, streamMetadata.getStreamerName()));

                                        log.info("Try to start record");

                                        //check for notification duplicate
                                        log.info("check for duplicate notification");
                                        RecordThread recordThread = applicationContext.getBean(RecordThread.class);
                                        recordThread.start(streamMetadata);
                                    } else {
                                        log.warn("Stream duplicate. Skip...");
                                    }
                                } catch (InterruptedException e) {
                                    log.error("DB error ", e);
                                } catch (StreamNotFoundException streamNotFoundException) {
                                    streamNotFoundException.printStackTrace();
                                }


                            }).start();

                            String startedAt = notificationModel.getStarted_at();
                            log.info("Record started at: {}", startedAt);
                        } else {
                            log.error("vodId is null. Stream not found");
                        }
                    } else {
                        log.info("stream duplicate");
                    }
                } else {
                    log.info("Stream down notification");
                }

            } else {
                log.warn("Subscription for {} canceled", user);
            }
        } else {
            log.error("Notification not accepted. Wrong hash.");
        }
        log.debug("Response ok");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}


