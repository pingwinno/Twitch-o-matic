package com.pingwinno.controllers;


import com.pingwinno.notification.handler.CommandLineRunner;
import com.pingwinno.notification.handler.DataModel;
import com.pingwinno.notification.handler.NotificationModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.io.IOException;


@Path("/handler")

public class HttpRequestHandler {


    String lastNotificationId;


    @GET
    public Response getQuery(@Context UriInfo info) {
        Response  response = null;
        if (info.getQueryParameters() != null) {

            MultivaluedMap<String, String> responseParameters = info.getQueryParameters();
            //handle denied response
            if (responseParameters.get("hub.mode").equals("denied")) {
                response = Response.status(Response.Status.OK).build();
                System.out.println("denied");
            }
            //handle verify response
            else if ((responseParameters.get("hub.mode").equals("subscribe"))) {
                response = Response.status(Response.Status.OK).entity(responseParameters.get("hub.challenge")).build();
                System.out.println("accepted");
            }
        } else {
            System.out.println("response is't correct");
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotificationJSON(DataModel dataModel) {

        System.out.println("Stream started");
        NotificationModel[] notificationArray = dataModel.getData();
        if (notificationArray[0] != null) {
            NotificationModel notificationModel = notificationArray[0];
            //check for notification duplicate
            if (!(notificationModel.getId().equals(lastNotificationId))) {
                System.out.println("Try to start stream recording");
                lastNotificationId = notificationModel.getId();
                CommandLineRunner cliInterface = new CommandLineRunner();
                new Thread(() -> {
                    try {
                        cliInterface.executeCommand(notificationModel.getTitle(), notificationModel.getStarted_at());
                    } catch (InterruptedException e) {
                        System.out.println("Can't start record thread");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("Can't start record thread");
                        e.printStackTrace();
                    }
                }).start();
            }

        } else System.out.println("Stream ended");

        return Response.status(Response.Status.ACCEPTED).build();
    }


}


