package com.pingwinno.presentation.twitch.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.DateConverter;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.HashHandler;
import com.pingwinno.infrastructure.RecordStatusList;
import com.pingwinno.infrastructure.SettingsProperties;
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


@Path("/handler")
public class TwitchApiHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private static String lastVodId;
    
    @GET
    public Response getSubscriptionQuery(@Context UriInfo info) {
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
                log.info(" Twith-o-matic started. Waiting for stream up");
            }
        } else log.warn("Subscription query is not correct. Try restart Twitch-o-matic.");

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(String stringDataModel
            , @HeaderParam("X-Hub-Signature") String signature) throws IOException, InterruptedException, SQLException {
        log.debug("Incoming stream up/down notification");

        if (HashHandler.compare(signature, stringDataModel)) {
            log.debug("Hash confirmed");
            StreamStatusNotificationModel dataModel =
                    new ObjectMapper().readValue(stringDataModel, StreamStatusNotificationModel.class);
            NotificationDataModel[] notificationArray = dataModel.getData();
            if (notificationArray.length > 0) {
                log.info("Stream is up");
                NotificationDataModel notificationModel = notificationArray[0];
                StreamExtendedDataModel streamMetadata = VodMetadataHelper.getLastVod(SettingsProperties.getUser());
                //check for notification duplicate
                log.info("check for duplicate notification");
                if (notificationModel.getType().equals("live")) {
                    if (streamMetadata.getVodId() != null) {
                    streamMetadata.setUuid(StorageHelper.getUuidName());

                    new RecordStatusList().addStatus
                            (new StatusDataModel(streamMetadata.getVodId(), StartedBy.WEBHOOK, DateConverter.convert(LocalDateTime.now()),
                                    State.INITIALIZE, streamMetadata.getUuid()));

                    log.info("Try to start record");
                    VodDownloader vodDownloader = new VodDownloader();

                        new Thread(() -> {
                            try {
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


