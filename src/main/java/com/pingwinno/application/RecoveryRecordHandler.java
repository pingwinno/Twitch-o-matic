package com.pingwinno.application;

import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.io.IOException;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    public static void recoverUncompletedRecordTask() throws IOException {
        if (RecordTaskHandler.loadTaskList()){
            VodDownloader vodDownloader = new VodDownloader();
            LinkedList<RecordTaskModel> recordTasks = new RecordTaskList().getTaskList();
            for (RecordTaskModel recordTaskModel : recordTasks){
                vodDownloader.initializeDownload(recordTaskModel);
            }
        }
    }
}
