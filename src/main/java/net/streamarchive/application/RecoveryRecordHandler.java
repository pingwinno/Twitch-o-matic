package net.streamarchive.application;

import net.streamarchive.application.twitch.handler.VodMetadataHelper;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.handlers.misc.AfterApplicationStartupRunnable;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.repository.StatusRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecoveryRecordHandler implements AfterApplicationStartupRunnable {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());

    private final
    VodMetadataHelper vodMetadataHelper;

    private StatusRepository statusRepository;

    @Autowired
    RecordThread recordThread;

    public RecoveryRecordHandler(VodMetadataHelper vodMetadataHelper) {
        this.vodMetadataHelper = vodMetadataHelper;
    }

    @Autowired
    public void setStatusRepository(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }


    public void recoverUncompletedRecordTask() {

        log.debug("Recovering uncompleted task...");
        if (!statusRepository.findByState(State.RUNNING).isEmpty()) {
            List<StatusDataModel> dataModels = statusRepository.findByState(State.RUNNING);

            if (!dataModels.isEmpty()) {
                for (StatusDataModel dataModel : dataModels) {
                    try {
                        log.info("Found uncompleted task. {}", dataModel.getVodId());
                        StreamDataModel streamDataModel;
                        streamDataModel = vodMetadataHelper.getVodMetadata(dataModel.getVodId());
                        streamDataModel.setUuid(dataModel.getUuid());
                        streamDataModel.setStreamerName(dataModel.getUser());
                        new Thread(() -> {
                            recordThread.start(streamDataModel);
                        }).start();
                    } catch (StreamNotFoundException streamNotFoundException) {
                        log.warn("Stream {} not found. Delete stream...", dataModel.getVodId());
                        statusRepository.delete(dataModel);
                    }
                }
            }
        } else log.info("Nothing to recover.");

    }


    @Override
    public void run() {
        recoverUncompletedRecordTask();
    }
}
