package net.streamarchive.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.presentation.management.api.StreamStatusSocketApi;
import net.streamarchive.repository.StatusRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
public class RecordStatusList {
    @Autowired
    StatusRepository statusRepository;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    @Transactional
    synchronized public void addStatus(StatusDataModel statusDataModel) throws IOException {
        log.trace("add status");
        log.trace("{}", statusDataModel);

        statusRepository.saveAndFlush(statusDataModel);
        StreamStatusSocketApi.updateState(new ObjectMapper().writeValueAsString(statusDataModel));
    }
    @Transactional
    synchronized public void changeState(UUID uuid, State state) throws IOException {
        if (statusRepository.existsByUuid(uuid)) {
            StatusDataModel updatedStatusDataModel = statusRepository.findByUuid(uuid).get(0);
            updatedStatusDataModel.setState(state);
            statusRepository.saveAndFlush(updatedStatusDataModel);
            StreamStatusSocketApi.updateState(new ObjectMapper().writeValueAsString(updatedStatusDataModel));
        } else log.error("Can't change state. Record {} not exist.", uuid);
    }
}
