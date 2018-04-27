package com.pingwinno.res;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/handler")

    public class NotificationfHandler
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
    response = Response .status(200).build();
    System.out.println(hubMode + " " + hubReason);
    System.out.println("denied");
}
//handle verify response
else
{
    hubChallenge = info.getQueryParameters().getFirst("hub.challenge");
   response = Response.status(202).entity(hubChallenge).build();
    System.out.println(hubMode + " " + hubChallenge);
   System.out.println("accepted");
}

        return response;


    }



}


