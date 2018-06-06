package com.pingwinno.presentation;


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
        if (notificationArray[0] != null) {
            NotificationDataModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if (!(notificationModel.getId().equals(lastNotificationId))) {
                lastNotificationId = notificationModel.getId();
                CommandLineRunner commandLineRunner = new CommandLineRunner();
                new Thread(() -> commandLineRunner.executeCommand(notificationModel.getStarted_at(), SettingsProperties.getUser())).start();
                String startedAt = notificationModel.getStarted_at();
                System.out.println(startedAt);
            }
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}


