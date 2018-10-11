package com.pingwinno.presentation;


import com.pingwinno.application.DateConverter;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.UserIdGetter;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.DataBaseHandler;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;


@Path("/handler")
public class TwitchApiHandler {
    private static Logger log = Logger.getLogger(TwitchApiHandler.class.getName());
    private String lastNotificationId;


    @GET
    public Response getSubscriptionQuery(@Context UriInfo info) {
        Response response = null;
        log.fine("Incoming challenge request");
        if (info != null) {
            String hubMode = info.getQueryParameters().getFirst("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = info.getQueryParameters().getFirst("hub.reason");
                response = Response.status(Response.Status.OK).build();
                log.warning("Subscription failed. Reason:" + hubReason);
                return response;
            }
            //handle verify response
            else {
                String hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
                response = Response.status(Response.Status.OK).entity(hubChallenge).build();
                log.fine("Subscription complete" + hubMode + " hub.challenge is:" + hubChallenge);
                log.info(" Twith-o-matic started. Waiting for stream up");
            }
        } else log.warning("Subscription query is not correct. Try restart Twitch-o-matic.");

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(StreamStatusNotificationModel dataModel) throws IOException, InterruptedException {
        log.fine("Incoming stream up/down notification");
        NotificationDataModel[] notificationArray = dataModel.getData();
        if (notificationArray.length > 0) {
            log.info("Stream is up");
            NotificationDataModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if ((!(notificationModel.getId().equals(lastNotificationId))) &&
                    //filter for live streams
                    (notificationModel.getType().equals("live")) &&
                    (notificationModel.getUser_id().equals(UserIdGetter.getUserId(SettingsProperties.getUser())))) {
                lastNotificationId = notificationModel.getId();
                UUID uuid = StorageHelper.getUuidName();
                StreamMetadataModel streamMetadata = VodMetadataHelper.getLastVod(SettingsProperties.getUser());

                StreamDataDBModel streamDataDBModel = new StreamDataDBModel(uuid, DateConverter.convert(streamMetadata.getDate()),
                        streamMetadata.getTitle(),streamMetadata.getGame());
                DataBaseHandler dataBaseHandler = new DataBaseHandler(streamDataDBModel);
                dataBaseHandler.writeToLocalDB();
                log.info("Try to start record");
                VodDownloader vodDownloader = new VodDownloader();
                if (streamMetadata.getVodId() != null) {
                    RecordTaskModel recordTask = new RecordTaskModel(uuid, streamMetadata.getVodId());
                    new Thread(() -> vodDownloader.initializeDownload(recordTask)).start();

                    String startedAt = notificationModel.getStarted_at();
                    log.info("Record started at: " + startedAt);
                }
                else {
                    log.severe("vodId is null. Stream not found");
                }
            }
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}


