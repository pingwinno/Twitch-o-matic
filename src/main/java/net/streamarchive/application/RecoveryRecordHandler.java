package net.streamarchive.application;

import net.streamarchive.application.twitch.playlist.handler.VodMetadataHelper;
import net.streamarchive.domain.VodRecorder;
import net.streamarchive.infrastructure.RecordStatusList;
import net.streamarchive.infrastructure.RecordThread;
import net.streamarchive.infrastructure.StreamNotFoundExeption;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.repository.StatusRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecoveryRecordHandler {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(RecoveryRecordHandler.class.getName());
    @Autowired
    RecordThread recordThread;

    private StatusRepository statusRepository;
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
                        streamDataModel = VodMetadataHelper.getVodMetadata(dataModel.getVodId());
                        streamDataModel.setUuid(dataModel.getUuid());
                        recordThread.start(streamDataModel);
                    } catch (StreamNotFoundExeption streamNotFoundExeption) {
                        log.warn("Stream {} not found. Delete stream...", dataModel.getVodId());
                        statusRepository.delete(dataModel);
                    } catch (InterruptedException | IOException e) {
                        log.error("Can't recover stream recording {}", dataModel.getUuid(), e);
                    }
                }
            }
        } else log.info("Nothing to recover.");
    }
}
