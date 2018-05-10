package com.pingwinno.controllers;


import com.pingwinno.notification.handler.CliInterface;
import com.pingwinno.notification.handler.CommandLineRunner;
import com.pingwinno.notification.handler.DataModel;
import com.pingwinno.notification.handler.NotificationModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/handler")

public class HttpRequestHandler {


    String lastNotificationId;


    @GET
    public Response getQuery(@Context UriInfo info) {

        Response response = null;
        if (info != null) {

            String hubMode = info.getQueryParameters().getFirst("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = info.getQueryParameters().getFirst("hub.reason");
                response = Response.status(Response.Status.OK).build();
                System.out.println(hubMode + " " + hubReason);
                System.out.println("denied");
                return response;
            }
//handle verify response
            else {
                String hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
                response = Response.status(Response.Status.OK).entity(hubChallenge).build();
                System.out.println(hubMode + " " + hubChallenge);
                System.out.println("accepted");

            }
        }
        else System.out.println("Response is not correct");
            return response;

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotificationJSON(DataModel dataModel) {
        System.out.println("POST");
        NotificationModel[] notificationArray = dataModel.getData();
        if (notificationArray[0] != null) {
            NotificationModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if (!(notificationModel.getId().equals(lastNotificationId))) {

                CommandLineRunner commandLineRunner = new CommandLineRunner();

                new Thread(() -> commandLineRunner.executeCommand(notificationModel.getTitle(), notificationModel.getStarted_at())).start();

                String startedAt = notificationModel.getStarted_at();
                System.out.println(startedAt);
            }
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }


}


