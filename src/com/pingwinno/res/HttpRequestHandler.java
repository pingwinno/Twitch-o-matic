package com.pingwinno.res;


import com.google.gson.Gson;
import com.pingwinno.notification_handler.DataModel;
import com.pingwinno.notification_handler.NotificationModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



@Path("/handler")

    public class HttpRequestHandler
{

   private Response response;
   private String hubMode;
   private String hubChallenge;
   private String hubReason;


@GET
    public Response getQuery (@Context UriInfo info)
    {
 hubMode = info.getQueryParameters().getFirst("hub.mode");



 //handle denied response
if (hubMode.equals("denied"))
{
    hubReason = info.getQueryParameters().getFirst("hub.reason");
    response = Response .status(Response.Status.OK).build();
    System.out.println(hubMode + " " + hubReason);
    System.out.println("denied");
    return response;
}
//handle verify response
else
{
    hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
   response = Response.status(Response.Status.OK).entity(hubChallenge).build();
    System.out.println(hubMode + " " + hubChallenge);
   System.out.println("accepted");
}
        return response;
    }



@POST
@Consumes(MediaType.APPLICATION_JSON)
public void postClichedMessage(String message) {
    Gson gson = new Gson();
    DataModel dataModel = gson.fromJson(message,  DataModel.class);

    NotificationModel [] notificationArray = dataModel.getData();
    NotificationModel notificationModel = notificationArray[0];
    String startedAt = notificationModel.getStarted_at();
    System.out.println(startedAt);

}
   /* @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotificationJSON (DataModel dataModel)
{
    System.out.println("POST");
    System.out.println(dataModel.getData());
    return Response.status(Response.Status.ACCEPTED).build();
}
*/

}


