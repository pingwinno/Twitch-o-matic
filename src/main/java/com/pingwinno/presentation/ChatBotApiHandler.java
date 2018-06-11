package com.pingwinno.presentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/bot_api")
public class ChatBotApiHandler {


    @Path("/auth")
    @GET
    public Response getAuthentication(@Context UriInfo info) {
return null;
    }

}
