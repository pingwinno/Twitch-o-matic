package com.pingwinno.presentation;


import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.domain.StreamlinkRunner;
import com.pingwinno.infrastructure.StreamStatusNotificationModel;
import com.pingwinno.infrastructure.NotificationDataModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/handler")
public class TwitchApiHandler {

    private String lastNotificationId;
    private String recordedStreamFileName;

    @GET
    public Response getSubscriptionQuery(@Context UriInfo info) {
        Response response = null;
        System.out.println("Incoming challenge request");
        if (info != null) {
            String hubMode = info.getQueryParameters().getFirst("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = info.getQueryParameters().getFirst("hub.reason");
                response = Response.status(Response.Status.OK).build();
                System.out.println("Subscription failed. Reason:" + hubReason);
                return response;
            }
            //handle verify response
            else {
                String hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
                response = Response.status(Response.Status.OK).entity(hubChallenge).build();
                System.out.println("Subscription complete" + hubMode + " hub.challenge is:" + hubChallenge);
            }
        } else System.out.println("Subscription query is not correct. Try restart Twitch-o-matic.");

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(StreamStatusNotificationModel dataModel) {
        System.out.println("Incoming stream up/down notification");
        NotificationDataModel[] notificationArray = dataModel.getData();
        if (notificationArray.length > 0) {
            NotificationDataModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if (!(notificationModel.getId().equals(lastNotificationId))) {
                lastNotificationId = notificationModel.getId();
                recordedStreamFileName = notificationModel.getTitle() + notificationModel.getStarted_at() + ".mp4";
                StreamlinkRunner commandLineRunner = new StreamlinkRunner();
                System.out.println("Try to start streamlink");

                new Thread(() -> commandLineRunner.runStreamlink(recordedStreamFileName, SettingsProperties.getRecordedStreamPath(), SettingsProperties.getUser())).start();

                String startedAt = notificationModel.getStarted_at();
                System.out.println("Record started:" +startedAt);

            }

        }
        else  {
            System.out.println("Stream ended. Uploading record");

            //place for google photo uploader
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}


