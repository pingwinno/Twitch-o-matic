package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.domain.DataBaseWriter;
import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * Method does import to local json files from MongoDB
     *
     * @return result of import
     */
    @GET
    @Path("/import")
    @Produces(MediaType.APPLICATION_JSON)
    public Response importToLocalDb() {
        try {
            for (String user : SettingsProperties.getUsers()) {
                for (StreamDocumentModel stream : (ArrayList<StreamDocumentModel>) MongoDBHandler.getCollection(user, StreamDocumentModel.class).find().into(new ArrayList<StreamDocumentModel>())) {
                    DataBaseWriter.writeToRemoteDB(stream, user);
                }
            }

            return Response.status(Response.Status.OK).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Method does export from local db to MongoDB
     *
     * @return of export
     */
    @GET
    @Path("/export")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportFromLocalDb() {
        ObjectMapper objectMapper = new ObjectMapper();

        for (String user : SettingsProperties.getUsers()) {

            try (Stream<java.nio.file.Path> walk = Files.walk(Paths.get(SettingsProperties.getRecordedStreamPath() + user))) {

                List<String> result = walk.filter(Files::isDirectory)
                        .map(x -> x.toString()).collect(Collectors.toList());

                result.forEach(x -> {
                    try {
                        DataBaseWriter.writeToRemoteDB(objectMapper.readValue(x, StreamDocumentModel.class), user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.status(Response.Status.OK).build();
    }

}
