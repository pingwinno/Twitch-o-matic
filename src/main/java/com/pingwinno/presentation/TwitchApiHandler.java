package com.pingwinno.presentation;


import com.pingwinno.application.DownloaderSelector;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.StreamFileNameHelper;
import com.pingwinno.application.twitch.playlist.handler.UserIdGetter;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.models.NotificationDataModel;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamStatusNotificationModel;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/handler")
public class TwitchApiHandler {
    private static Logger log = Logger.getLogger(TwitchApiHandler.class.getName());
    private String lastNotificationId;
    private String recordedStreamFilePath;
    private VodDownloader vodDownloader = new VodDownloader();
    private String streamName;

    @GET
    public Response getSubscriptionQuery(@Context UriInfo info) {
        Response response = null;
        log.info("Incoming challenge request");
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
                log.info("Subscription complete" + hubMode + " hub.challenge is:" + hubChallenge);
            }
        } else log.warning("Subscription query is not correct. Try restart Twitch-o-matic.");

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(StreamStatusNotificationModel dataModel) throws IOException {
        log.info("Incoming stream up/down notification");
        NotificationDataModel[] notificationArray = dataModel.getData();
        if (notificationArray.length > 0) {
            NotificationDataModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if ((!(notificationModel.getId().equals(lastNotificationId))) &&
                    (notificationModel.getType().equals("live")) &&
                    (notificationModel.getUser_id().equals(UserIdGetter.getUserId(SettingsProperties.getUser())))) {
                lastNotificationId = notificationModel.getId();
                streamName = StreamFileNameHelper.makeStreamName(notificationModel.getTitle(),
                        notificationModel.getStarted_at());
                log.info("File name is: "+ recordedStreamFilePath);
                log.info("Try to start streamlink");
                StorageHelper.cleanUpStorage();
                new Thread(() -> DownloaderSelector.runDownloader(streamName)).start();

                String startedAt = notificationModel.getStarted_at();
                log.info("Record started at: " + startedAt);
                log.info(recordedStreamFilePath);
                }

        } else {

        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}


