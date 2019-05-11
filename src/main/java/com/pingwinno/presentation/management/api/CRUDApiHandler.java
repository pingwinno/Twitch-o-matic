package com.pingwinno.presentation.management.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.DateConverter;
import com.pingwinno.application.JdbcHandler;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.domain.VodRecorder;
import com.pingwinno.infrastructure.RecordStatusList;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.StreamNotFoundExeption;
import com.pingwinno.infrastructure.enums.StartedBy;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.AddDataModel;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.infrastructure.models.StreamDataModel;
import com.pingwinno.infrastructure.models.ValidationDataModel;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Path("/database")
public class CRUDApiHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Path("/add")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response startRecord(AddDataModel dataModel) {
        Response response;
        StreamDataModel streamMetadata = null;
        try {
            log.trace("type: {}", dataModel.getType());
            log.trace("value: {}", dataModel.getValue());
            if (dataModel.getType().equals("user")) {
                streamMetadata = VodMetadataHelper.getLastVod(dataModel.getValue());
            } else if (dataModel.getType().equals("vod")) {
                streamMetadata = VodMetadataHelper.getVodMetadata(dataModel.getValue());
            }
            if (streamMetadata != null) {
                VodRecorder vodRecorder = new VodRecorder();
                if (streamMetadata.getVodId() != null) {
                    if (new JdbcHandler().search("vodId", "vodId",
                            streamMetadata.getVodId()).contains(streamMetadata.getVodId())) {
                        log.debug("Stream exist. Run validation or delete folder");
                        return Response.notModified().build();
                    } else {
                        streamMetadata.setUuid(StorageHelper.getUuidName());
                    }

                    StreamDataModel finalStreamMetadata = streamMetadata;
                    streamMetadata.setSkipMuted(dataModel.isSkipMuted());

                    new RecordStatusList().addStatus
                            (new StatusDataModel(streamMetadata.getVodId(), StartedBy.MANUAL, DateConverter.convert(LocalDateTime.now()),
                                    State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));

                    new Thread(() -> vodRecorder.start(finalStreamMetadata)).start();

                    Date startedAt = streamMetadata.getDate();
                    log.info("Record started at:{} ", startedAt);
                    response = Response.accepted().build();
                } else {
                    log.error("Stream {] not found", dataModel.getValue());
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (IOException | InterruptedException | StreamNotFoundExeption e) {
            response = Response.status(500, e.toString()).build();
            log.error("Can't start record {}", e);
        }
        return response;
    }

    @Path("/validate")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response validateRecord(ValidationDataModel dataModel) {
        if (!(dataModel.getVodId().trim().equals("") && dataModel.getUuid() == null)) {
            Response response;
            StreamDataModel streamMetadata;
            try {
                log.trace("vodId: {}", dataModel.getVodId());
                log.trace("uuid: {}", dataModel.getUuid());

                streamMetadata = VodMetadataHelper.getVodMetadata(dataModel.getVodId());

                if (streamMetadata != null) {
                    VodRecorder vodRecorder = new VodRecorder();
                    if (streamMetadata.getVodId() != null) {

                        streamMetadata.setUuid(dataModel.getUuid());
                        StreamDataModel finalStreamMetadata = streamMetadata;
                        streamMetadata.setSkipMuted(dataModel.isSkipMuted());

                        new RecordStatusList().addStatus
                                (new StatusDataModel(streamMetadata.getVodId(), StartedBy.VALIDATION, DateConverter.convert(LocalDateTime.now()),
                                        State.INITIALIZE, streamMetadata.getUuid(), streamMetadata.getUser()));

                        new Thread(() -> vodRecorder.start(finalStreamMetadata)).start();

                        Date startedAt = streamMetadata.getDate();
                        log.info("Record started at:{} ", startedAt);
                        response = Response.accepted().build();
                    } else {
                        log.error("Stream {] not found", dataModel.getVodId());
                        response = Response.status(Response.Status.NOT_FOUND).build();
                    }
                } else {
                    response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
            } catch (IOException | InterruptedException | StreamNotFoundExeption e) {
                response = Response.status(500, e.toString()).build();
                log.error("Can't start record {}", e);
            }
            return response;
        } else {
            return Response.noContent().build();
        }
    }

    @Path("/{user}/{uuid}")
    @DELETE
    public Response deleteStream(@PathParam("uuid") String uuid, @PathParam("user") String user, @QueryParam("deleteMedia") String deleteMedia) {

        MongoDBHandler.getCollection(user).deleteOne(new Document("_id", uuid));
        log.info("delete stream {}", uuid);
        if (deleteMedia.equals("true")) {
            try {
                FileUtils.deleteDirectory(new File(SettingsProperties.getRecordedStreamPath() + "" + user + "/" + uuid));
            } catch (IOException e) {
                log.error("can't delete media {] ", e);
                return Response.notModified().build();
            }
        }

        return Response.accepted().build();
    }

    @Path("/update/{user}/{uuid}")
    @POST
    public Response updateStream(@PathParam("user") String user, @PathParam("uuid") String uuid, StreamDataModel dataModel) {

        MongoDBHandler.getCollection(user).updateOne(eq("_id", uuid),
                combine(set("date", dataModel.getDate()), set("title", dataModel.getTitle()), set("game", dataModel.getGame())));

        return Response.ok().build();

    }

    @Path("/streams/{user}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStreamsList(@PathParam("user") String user) {
        log.trace(user);

        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(MongoDBHandler.getCollection(user).
                            find().projection(fields(include("title", "date", "game"))).into(new ArrayList()))
                            .replaceAll("_id", "uuid")).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersList() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(SettingsProperties.getUsers())).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
