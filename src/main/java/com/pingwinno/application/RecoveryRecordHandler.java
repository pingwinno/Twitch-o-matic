package com.pingwinno.application;

import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());
    public static void recoverUncompletedRecordTask() throws IOException {
        log.debug("Recovering uncompleted task...");
        if (RecordTaskHandler.loadTaskList()) {
            VodDownloader vodDownloader = new VodDownloader();
            LinkedList<StreamExtendedDataModel> recordTasks = new RecordTaskList().getTaskList();
            for (StreamExtendedDataModel recordTaskModel : recordTasks) {
                vodDownloader.initializeDownload(recordTaskModel);
            }
        }
    }
}
