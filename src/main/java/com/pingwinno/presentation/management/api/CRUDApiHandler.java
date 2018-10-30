package com.pingwinno.presentation.management.api;


import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.SqliteHandler;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Path("/")
public class ManagementApiHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Path("/start")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response startRecord(@QueryParam("type") String type,
                                @QueryParam("value") String value, @QueryParam("skip_muted") String skipMuted) {
        Response response;
        StreamExtendedDataModel streamMetadata = null;
        try {
            log.trace("type: {}",type);
            log.trace("value: {}",value);
            if (type.equals("user")) {
                streamMetadata = VodMetadataHelper.getLastVod(value);
            } else if (type.equals("vod")) {
                streamMetadata = VodMetadataHelper.getVodMetadata(value);
            }
            if (streamMetadata != null) {
                VodDownloader vodDownloader = new VodDownloader();
                if (streamMetadata.getVodId() != null) {

                    streamMetadata.setUuid(StorageHelper.getUuidName());
                    StreamExtendedDataModel finalStreamMetadata = streamMetadata;
                    streamMetadata.setSkipMuted(skipMuted.equals("true"));
                    new Thread(() -> vodDownloader.initializeDownload(finalStreamMetadata)).start();

                    String startedAt = streamMetadata.getDate();
                    log.info("Record started at:{} ", startedAt);
                    response = Response.accepted().build();
                } else {
                    log.error("Stream {] not found", value);
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (IOException | InterruptedException e) {
            response = Response.status(500, e.toString()).build();
            log.error("Can't start record {}", e);
        }
        return response;
    }

    @Path("/validate")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response validateRecord(@QueryParam("vodId") String vodId,
                                   @QueryParam("uuid") String uuid, @QueryParam("skip_muted") String skipMuted) {
        Response response;
        StreamExtendedDataModel streamMetadata = null;
        try {
            log.trace("vodId: {}",vodId);
            log.trace("uuid: {}",uuid);

                streamMetadata = VodMetadataHelper.getVodMetadata(vodId);

            if (streamMetadata != null) {
                VodDownloader vodDownloader = new VodDownloader();
                if (streamMetadata.getVodId() != null) {

                    streamMetadata.setUuid(UUID.fromString(uuid));
                    StreamExtendedDataModel finalStreamMetadata = streamMetadata;
                    streamMetadata.setSkipMuted(skipMuted.equals("true"));
                    new Thread(() -> vodDownloader.initializeDownload(finalStreamMetadata)).start();

                    String startedAt = streamMetadata.getDate();
                    log.info("Record started at:{} ", startedAt);
                    response = Response.accepted().build();
                } else {
                    log.error("Stream {] not found", vodId);
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (IOException | InterruptedException e) {
            response = Response.status(500, e.toString()).build();
            log.error("Can't start record {}", e);
        }
        return response;
    }

    @Path("/delete")
    @DELETE
    public Response deleteStream(@QueryParam("uuid") String uuid, @QueryParam("delete_media") String deleteMedia) {

        SqliteHandler sqliteHandler = new SqliteHandler();
        sqliteHandler.delete("uuid", uuid);
        log.info("delete stream {}", uuid);
        if (deleteMedia.equals("true")) {
            try {
                FileUtils.deleteDirectory(new File(SettingsProperties.getRecordedStreamPath() + uuid));
            } catch (IOException e) {
                log.error("can't delete media {] ", e);
                return Response.notModified().build();
            }
        }

        return Response.accepted().build();
    }


}
