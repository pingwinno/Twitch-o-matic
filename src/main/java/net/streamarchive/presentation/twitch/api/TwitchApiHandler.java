package net.streamarchive.presentation.twitch.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.DateConverter;
import net.streamarchive.repository.StatusRepository;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.SubscriptionRequestTimer;
import net.streamarchive.application.twitch.playlist.handler.RecordStatusGetter;
import net.streamarchive.application.twitch.playlist.handler.VodMetadataHelper;
import net.streamarchive.domain.VodRecorder;
import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.StreamNotFoundExeption;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.NotificationDataModel;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.StreamStatusNotificationModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@Path("/handler/{user}")
public class TwitchApiHandler {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    RecordStatusList recordStatusList;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @GET
    public Response getSubscriptionQuery(@Context UriInfo info, @PathParam("user") String user) {
        Response response = null;
        log.debug("Incoming challenge request");
        if (info != null) {
            log.debug("hub.mode {} ", info.getQueryParameters().getFirst("hub.mode"));
            String hubMode = info.getQueryParameters().getFirst("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = info.getQueryParameters().getFirst("hub.reason");
                response = Response.status(Response.Status.OK).build();
                log.warn("Subscription failed. Reason:{}", hubReason);
                return response;
            }
            //handle verify response
            else {
                String hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
                response = Response.status(Response.Status.OK).entity(hubChallenge).build();
                log.debug("Subscription complete {} hub.challenge is:{}", hubMode, hubChallenge);
                log.info(" Twith-o-matic started for {}. Waiting for stream up", user);
            }
        } else log.warn("Subscription query is not correct. Try restart Twitch-o-matic.");

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(String stringDataModel
            , @HeaderParam("X-Hub-Signature") String signature, @PathParam("user") String user) throws IOException,
            InterruptedException, SQLException, StreamNotFoundExeption {
        log.debug("Incoming stream up/down notification");

        if (HashHandler.compare(signature, stringDataModel)) {
            //check for active subscription
            if (SubscriptionRequestTimer.getTimers().containsKey(user)) {
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

                                        new RecordStatusList().addStatus
                                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.WEBHOOK, DateConverter.convert(LocalDateTime.now()),
                                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));

                                        log.info("Try to start record");
                                        VodRecorder vodRecorder = new VodRecorder(recordStatusList);

                                        //check for notification duplicate
                                        log.info("check for duplicate notification");
                                        vodRecorder.start(streamMetadata);
                                    } else log.warn("Stream duplicate. Skip...");
                                } catch (IOException | InterruptedException e) {
                                    log.error("DB error {} ", e);
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
}


