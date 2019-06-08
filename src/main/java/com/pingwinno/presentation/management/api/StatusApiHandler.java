package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.JdbcHandler;
import com.pingwinno.infrastructure.RecordThreadSupervisor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * This API uses for records statuses.
 * Endpoint {@code /status_list}
 */

@Path("/status_list")
public class StatusApiHandler {
    /**
     * This method returns list of record task
     *
     * @return list of record tasks
     */
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

    /**
     * This method stops stream task.
     * @param uuid UUID of stream
     * @return stops stream record with {@code uuid}
     */
    @DELETE
    @Path("/{uuid}")
    public Response stopTask(@PathParam("uuid") String uuid) {
        RecordThreadSupervisor.stop(UUID.fromString(uuid));
        return Response.accepted().build();
    }
}
