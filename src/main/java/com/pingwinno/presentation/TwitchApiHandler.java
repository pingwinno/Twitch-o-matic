package com.pingwinno.presentation;


import com.pingwinno.domain.GooglePhotosUploader;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.domain.CommandLineRunner;
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
        if (info != null) {
            String hubMode = info.getQueryParameters().getFirst("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = info.getQueryParameters().getFirst("hub.reason");
                response = Response.status(Response.Status.OK).build();
                System.out.println(hubMode + " " + hubReason);
                return response;
            }
            //handle verify response
            else {
                String hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
                response = Response.status(Response.Status.OK).entity(hubChallenge).build();
                System.out.println(hubMode + " " + hubChallenge);
            }
        } else System.out.println("Response is not correct");
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleStreamNotification(StreamStatusNotificationModel dataModel) {
        System.out.println("Handle POST request");
        NotificationDataModel[] notificationArray = dataModel.getData();
        if (notificationArray[0] != null) {
            NotificationDataModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if (!(notificationModel.getId().equals(lastNotificationId))) {
                lastNotificationId = notificationModel.getId();
                recordedStreamFileName = notificationModel.getTitle() + notificationModel.getStarted_at() + ".mp4";
                CommandLineRunner commandLineRunner = new CommandLineRunner();
                System.out.println("Try to start streamlink");
                new Thread(() -> commandLineRunner.executeCommand(notificationModel.getStarted_at(), SettingsProperties.getUser(), notificationModel.getTitle())).start();
                String startedAt = notificationModel.getStarted_at();
                System.out.println(startedAt);
            }

        }
        else {

            GooglePhotosUploader googlePhotosUploader = new GooglePhotosUploader();
            googlePhotosUploader.uploadRecordedStream(recordedStreamFileName);
            //place for google photo uploader
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}


