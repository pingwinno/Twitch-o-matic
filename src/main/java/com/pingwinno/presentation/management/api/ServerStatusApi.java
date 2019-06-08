package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.StorageHelper;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * API returns storage state.
 */
@Path("/server")
public class ServerStatusApi {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * Method returns list of free storage space per streamer.
     *
     * @return list of free storage space per streamer.
     */
    @GET
    @Path("/free_storage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreeStorage() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(StorageHelper.getFreeSpace())).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Method returns list of total storage capacity per streamer.
     * @return list of total storage capacity per streamer.
     */
    @GET
    @Path("/total_storage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalStorage() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(StorageHelper.getTotalSpace())).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
