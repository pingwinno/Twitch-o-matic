package com.pingwinno.presentation.twitch.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.DateConverter;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.RecordStatusGetter;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.HashHandler;
import com.pingwinno.infrastructure.RecordStatusList;
import com.pingwinno.infrastructure.StreamNotFoundExeption;
import com.pingwinno.infrastructure.enums.StartedBy;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.NotificationDataModel;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import com.pingwinno.infrastructure.models.StreamStatusNotificationModel;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


@Path("/handler/{user}")
public class TwitchApiHandler {
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
            , @HeaderParam("X-Hub-Signature") String signature, @PathParam("user") String user) throws IOException, InterruptedException, SQLException, StreamNotFoundExeption {
        log.debug("Incoming stream up/down notification");

        if (HashHandler.compare(signature, stringDataModel)) {
            log.debug("Hash confirmed");
            StreamStatusNotificationModel dataModel =
                    new ObjectMapper().readValue(stringDataModel, StreamStatusNotificationModel.class);
            NotificationDataModel[] notificationArray = dataModel.getData();
            if (notificationArray.length > 0) {
                log.info("Stream is up");
                NotificationDataModel notificationModel = notificationArray[0];
                StreamExtendedDataModel streamMetadata = VodMetadataHelper.getLastVod(user);

                //check for notification duplicate
                log.info("check for duplicate notification");
                if (notificationModel.getType().equals("live")) {
                    if (streamMetadata.getVodId() != null) {
                        int counter = 0;
                        while (RecordStatusGetter.getRecordStatus(streamMetadata.getVodId()).equals("recorded")) {
                            Thread.sleep(200 * 1000);
                            log.warn("vod is not created yet... cycle " + counter);
                            counter++;
                            if (counter > 6) {
                                throw new StreamNotFoundExeption("new vod not found");
                            }
                        }
                    streamMetadata.setUuid(StorageHelper.getUuidName());

                    new RecordStatusList().addStatus
                            (new StatusDataModel(streamMetadata.getVodId(), StartedBy.WEBHOOK, DateConverter.convert(LocalDateTime.now()),
                                    State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));

                    log.info("Try to start record");
                    VodDownloader vodDownloader = new VodDownloader();

                        new Thread(() -> {
                            try {
                                if (DateConverter.convert(notificationModel.getStarted_at()).equals(streamMetadata.getDate())) {
                                    log.warn("date equals: {} {}", notificationModel.getStarted_at(), streamMetadata.getDate());
                                }
                                vodDownloader.initializeDownload(streamMetadata);
                            } catch (SQLException e) {
                                log.error("DB error {} ",e);
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
            log.error("Notification not accepted. Wrong hash.");
        }
        log.debug("Response ok");
        return Response.status(Response.Status.OK).build();
    }
}


