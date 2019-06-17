package net.streamarchive.presentation.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.repository.StatusRepository;
import net.streamarchive.infrastructure.RecordThreadSupervisor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * This API uses for records statuses.
 * Endpoint {@code /status_list}
 */
@Service
@Path("/status_list")
public class StatusApiHandler {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    RecordThreadSupervisor recordThreadSupervisor;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * This method returns list of record task
     *
     * @return list of record tasks
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusList() {
        try {
            log.debug("Status list request");
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(statusRepository.findAll())).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * This method stops stream task.
     *
     * @param uuid UUID of stream
     * @return stops stream record with {@code uuid}
     */
    @DELETE
    @Path("/{uuid}")
    public Response stopTask(@PathParam("uuid") String uuid) {
        recordThreadSupervisor.stop(UUID.fromString(uuid));
        return Response.accepted().build();
    }
}
