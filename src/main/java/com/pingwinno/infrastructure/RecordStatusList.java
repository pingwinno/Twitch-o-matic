package com.pingwinno.infrastructure;

import com.pingwinno.application.JdbcHandler;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class RecordStatusList {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private static JdbcHandler dataHandler = new JdbcHandler();

    synchronized public void addStatus(StatusDataModel statusDataModel) {
        log.trace("add status");
        log.trace("{}", statusDataModel);

        log.trace("search query {} ", dataHandler.search("uuid", statusDataModel.getUuid().toString()));
        if (dataHandler.search("uuid", statusDataModel.getUuid().toString()).
                contains(statusDataModel)) {
            dataHandler.update(statusDataModel);
            log.trace("Status {} added", statusDataModel.getUuid());
        } else {
            dataHandler.insert(statusDataModel);
            log.trace("Status {} updated", statusDataModel.getUuid());
        }
    }

    synchronized public void changeState(UUID uuid, State state) {
        StatusDataModel updatedStatusDataModel;
        if ((updatedStatusDataModel = dataHandler.search("uuid", uuid.toString()).poll()) != null) {
            updatedStatusDataModel.setState(state);
            dataHandler.update(updatedStatusDataModel);
        } else log.error("Can't change state. Record {} not exist.", uuid);
    }
}
