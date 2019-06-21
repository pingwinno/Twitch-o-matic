package net.streamarchive.infrastructure;

import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.presentation.management.api.StreamStatusSocketApi;
import net.streamarchive.repository.StatusRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RecordStatusList {
    private final
    StatusRepository statusRepository;
    private final
    StreamStatusSocketApi streamStatusSocketApi;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public RecordStatusList(StatusRepository statusRepository, StreamStatusSocketApi streamStatusSocketApi) {
        this.statusRepository = statusRepository;
        this.streamStatusSocketApi = streamStatusSocketApi;
    }

    @Transactional
    synchronized public void addStatus(StatusDataModel statusDataModel) {
        log.trace("add status");
        log.trace("{}", statusDataModel);

        statusRepository.saveAndFlush(statusDataModel);
        streamStatusSocketApi.sendUpdate(statusDataModel);
    }

    @Transactional
    synchronized public void changeState(UUID uuid, State state) {
        if (statusRepository.existsByUuid(uuid)) {
            StatusDataModel updatedStatusDataModel = statusRepository.findByUuid(uuid).get(0);
            updatedStatusDataModel.setState(state);
            statusRepository.saveAndFlush(updatedStatusDataModel);
            streamStatusSocketApi.sendUpdate(updatedStatusDataModel);
        } else log.error("Can't change state. Record {} not exist.", uuid);
    }
}
