package com.pingwinno.presentation;


import com.pingwinno.application.DateConverter;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.DataBaseHandler;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.models.RecordTaskModel;
import com.pingwinno.infrastructure.models.StreamDataDBModel;
import com.pingwinno.infrastructure.models.StreamMetadataModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@Path("/start")
public class ManagementApi {
    private static Logger log = Logger.getLogger(ManagementApi.class.getName());
@GET
@Produces(MediaType.TEXT_HTML)
    @Path("{user}")
public Response startRecord(@PathParam("user") String user){
    Response response;
    try {
       StreamMetadataModel streamMetadata = VodMetadataHelper.getLastVod(user);
        UUID uuid = StorageHelper.getUuidName();
        VodDownloader vodDownloader = new VodDownloader();
        if (streamMetadata.getVodId() != null) {
            StreamDataDBModel streamDataDBModel = new StreamDataDBModel(uuid, DateConverter.convert(streamMetadata.getDate()),
                    streamMetadata.getTitle(),streamMetadata.getGame());
            DataBaseHandler dataBaseHandler = new DataBaseHandler(streamDataDBModel);
            dataBaseHandler.writeToLocalDB();
            RecordTaskModel recordTask = new RecordTaskModel(uuid, streamMetadata.getVodId());
            new Thread(() -> vodDownloader.initializeDownload(recordTask)).start();

            String startedAt = streamMetadata.getDate();
            log.info("Record started at: " + startedAt);
             response = Response.accepted().build();
        }
        else {
            log.severe("Stream not found");
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
    } catch (IOException | InterruptedException e) {
        response = Response.status(500, e.toString()).build();
        e.printStackTrace();
    }

    return response;
}


}
