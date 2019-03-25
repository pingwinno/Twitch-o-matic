package com.pingwinno.application;

import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.domain.sqlite.handlers.JdbcHandler;
import com.pingwinno.infrastructure.StreamNotFoundExeption;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.infrastructure.models.StreamDataModel;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());

    public static void recoverUncompletedRecordTask() {
        log.debug("Recovering uncompleted task...");
        LinkedList<StatusDataModel> dataModels = new JdbcHandler().selectAll();

        if (!dataModels.isEmpty()) {
            for (StatusDataModel dataModel:dataModels){
                if (dataModel.getState().equals(State.RUNNING)) {
                    try {
                        log.info("Found uncompleted task. {}", dataModel.getVodId());
                        StreamDataModel extendedDataModel;
                        extendedDataModel = VodMetadataHelper.getVodMetadata(dataModel.getVodId());
                        extendedDataModel.setUuid(dataModel.getUuid());
                        new VodDownloader().initializeDownload(extendedDataModel);
                    } catch (StreamNotFoundExeption streamNotFoundExeption) {
                        log.warn("Stream {} not found. Delete stream...", dataModel.getVodId());
                        new JdbcHandler().delete(dataModel.getUuid().toString());
                    } catch (InterruptedException | IOException | SQLException e) {
                        log.error("Can't recover stream recording {}", e);
                    }

                }
            }
        }
    }
}
