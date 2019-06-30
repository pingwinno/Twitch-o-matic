package net.streamarchive.presentation.twitch.api;


import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.repository.StatusRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/handler")
public class TwitchApiHandler {
    private final
    StatusRepository statusRepository;
    private final
    RecordStatusList recordStatusList;

    private final
    RecordThread recordThread;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public TwitchApiHandler(StatusRepository statusRepository, RecordStatusList recordStatusList, RecordThread recordThread) {
        this.statusRepository = statusRepository;
        this.recordStatusList = recordStatusList;
        this.recordThread = recordThread;
    }

    @RequestMapping("/{user}")
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
        } else log.warn("Subscription query is not correct. Try restart Twitch-o-matic.");

        return null;
    }
/*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(String stringDataModel
            , @HeaderParam("X-Hub-Signature") String signature, @PathVariable("user") String user) throws IOException,
            InterruptedException, StreamNotFoundExeption {
        log.debug("Incoming stream up/down notification");

        if (HashHandler.compare(signature, stringDataModel)) {
            //check for active subscription
            if (subscriptionRequestTimer.getTimers().containsKey(user)) {
                log.debug("Hash confirmed");
                StreamStatusNotificationModel dataModel =
                        new ObjectMapper().readValue(stringDataModel, StreamStatusNotificationModel.class);
                NotificationDataModel[] notificationArray = dataModel.getData();
                if (notificationArray.length > 0) {
                    log.info("Stream is up");
                    NotificationDataModel notificationModel = notificationArray[0];


                    if (notificationModel.getType().equals("live")) {

                        if (VodMetadataHelper.getLastVod(user).getVodId() != 0) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(10 * 1000);
                                    StreamDataModel streamMetadata = VodMetadataHelper.getLastVod(user);
                                    int counter = 0;
                                    log.trace(streamMetadata.toString());
                                    while (!RecordStatusGetter.isRecording(streamMetadata.getVodId())) {
                                        log.trace("vodId: {}", streamMetadata.getVodId());
                                        streamMetadata = VodMetadataHelper.getLastVod(user);
                                        log.info("waiting for creating vod...");
                                        Thread.sleep(20 * 1000);
                                        log.warn("vod is not created yet... cycle " + counter);
                                        counter++;
                                        if (counter > 60) {
                                            throw new StreamNotFoundExeption("new vod not found");
                                        }
                                    }


                                    if (statusRepository.existsById(streamMetadata.getVodId())) {
                                        streamMetadata.setUuid(StorageHelper.getUuidName());

                                        recordStatusList.addStatus
                                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.WEBHOOK, DateConverter.convert(LocalDateTime.now()),
                                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));

                                        log.info("Try to start record");

                                        //check for notification duplicate
                                        log.info("check for duplicate notification");
                                        recordThread.start(streamMetadata);
                                    } else log.warn("Stream duplicate. Skip...");
                                } catch (IOException | InterruptedException e) {
                                    log.error("DB error ", e);
                                } catch (StreamNotFoundExeption streamNotFoundExeption) {
                                    streamNotFoundExeption.printStackTrace();
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
        return Response.status(Response.Status.OK).build();
    }

 */
}


