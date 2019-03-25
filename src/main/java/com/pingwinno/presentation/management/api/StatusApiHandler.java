package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.domain.JdbcHandler;
import com.pingwinno.infrastructure.RecordThreadSupervisor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/status_api")
public class StatusApiHandler {
    @Path("/status_list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusList() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(new JdbcHandler().selectAll())).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{uuid}")
    public Response stopTask(@PathParam("uuid") String uuid) {
        RecordThreadSupervisor.changeFlag(UUID.fromString(uuid), false);
        return Response.accepted().build();
    }
}
