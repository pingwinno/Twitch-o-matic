package com.pingwinno.application;

import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.RecordStatusList;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.enums.StartedBy;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class RecoveryRecordHandler {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());
    public static void recoverUncompletedRecordTask() throws IOException {
        log.debug("Recovering uncompleted task...");
        if (RecordTaskHandler.loadTaskList()) {
            VodDownloader vodDownloader = new VodDownloader();
            LinkedList<StreamExtendedDataModel> recordTasks = new RecordTaskList().getTaskList();
            for (StreamExtendedDataModel recordTaskModel : recordTasks) {
                new RecordStatusList().addStatus
                        (new StatusDataModel(recordTaskModel.getVodId(), StartedBy.RECOVERY, DateConverter.convert(LocalDateTime.now()),
                                State.INITIALIZE, recordTaskModel.getUuid()));
                vodDownloader.initializeDownload(recordTaskModel);
            }
        }
    }
}
