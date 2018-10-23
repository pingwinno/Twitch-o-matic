package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.RecordStatusList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusApiHandler {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusList() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(new RecordStatusList().getStatusList())).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
