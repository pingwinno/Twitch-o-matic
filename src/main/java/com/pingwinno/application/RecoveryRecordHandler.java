package com.pingwinno.application;

import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;

import java.io.IOException;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    public static void recoverUncompletedRecordTask() throws IOException {
        if (RecordTaskHandler.loadTaskList()) {
            VodDownloader vodDownloader = new VodDownloader();
            LinkedList<StreamExtendedDataModel> recordTasks = new RecordTaskList().getTaskList();
            for (StreamExtendedDataModel recordTaskModel : recordTasks) {
                vodDownloader.initializeDownload(recordTaskModel);
            }
        }
    }
}
