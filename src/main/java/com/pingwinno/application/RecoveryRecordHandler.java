package com.pingwinno.application;

import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.domain.sqlite.handlers.SqliteStatusDataHandler;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());

    public static void recoverUncompletedRecordTask() throws SQLException, IOException, InterruptedException {
        log.debug("Recovering uncompleted task...");
        LinkedList<StatusDataModel> dataModels = new SqliteStatusDataHandler().selectAll();

        StatusDataModel dataModel;
        if (!dataModels.isEmpty()) {
            while ((dataModel = dataModels.get(dataModels.indexOf(dataModels.stream()
                    .filter(statusDataModel -> State.RUNNING.equals(statusDataModel.getState()))
                    .findAny()
                    .orElse(null)))) != null) {
                StreamExtendedDataModel extendedDataModel = VodMetadataHelper.getVodMetadata(dataModel.getVodId());
                extendedDataModel.setUuid(dataModel.getUuid());
                new VodDownloader().initializeDownload(extendedDataModel);
            }
        }
    }
}
