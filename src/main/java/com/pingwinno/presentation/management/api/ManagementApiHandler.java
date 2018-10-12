package com.pingwinno.presentation.management.api;


import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.DataBaseHandler;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@Path("/start")
public class ManagementApiHandler {
    private static Logger log = Logger.getLogger(ManagementApiHandler.class.getName());

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response startRecord(@QueryParam("type") String type, @QueryParam("value") String value) {
        Response response;
        StreamExtendedDataModel streamMetadata = null;
        try {

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
                    new Thread(() -> vodDownloader.initializeDownload(finalStreamMetadata)).start();

                    String startedAt = streamMetadata.getDate();
                    log.info("Record started at: " + startedAt);
                    response = Response.accepted().build();
                } else {
                    log.severe("Stream not found");
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (IOException | InterruptedException e) {
            response = Response.status(500, e.toString()).build();
            e.printStackTrace();
        }
        return response;
    }


}
