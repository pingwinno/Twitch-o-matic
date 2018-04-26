package com.pingwinno.res;

import org.eclipse.jetty.http.MetaData;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/handler")

    public class NotifHandler
{
    private int status;
   private Response response;
    String hubMode = " ";
    String hubChallange = " ";
    String hubReason = " ";


@GET
    public Response getQuery (@Context UriInfo info)
    {
 hubMode = info.getQueryParameters().getFirst("hub.mode");
 hubChallange = info.getQueryParameters().getFirst("hub.challenge");
 hubReason = info.getQueryParameters().getFirst("hub.reason");

 //handle denied response
if (hubMode.equals("denied"))
{
    status = 200;
    response = Response .status(200).build();
    System.out.println("denied");
}
//handle verify response
else
{
   response = Response.status(202).entity(hubChallange).build();
   System.out.println("accepted");
}

        System.out.println(hubMode + " " + hubChallange + " " + hubReason);
        return response;


    }



}


