package com.pingwinno.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.JdbcHandler;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import com.pingwinno.presentation.management.api.WebSocket;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class RecordStatusList {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private static JdbcHandler dataHandler = new JdbcHandler();

    synchronized public void addStatus(StatusDataModel statusDataModel) throws IOException {
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
        WebSocket.updateState(new ObjectMapper().writeValueAsString(statusDataModel));
    }

    synchronized public void changeState(UUID uuid, State state) throws IOException {
        StatusDataModel updatedStatusDataModel;
        if ((updatedStatusDataModel = dataHandler.search("uuid", uuid.toString()).poll()) != null) {
            updatedStatusDataModel.setState(state);
            dataHandler.update(updatedStatusDataModel);
            String jsonString = "{\n" +
                    "  \"uuid\": \"" + uuid.toString() + "\",\n" +
                    "  \"state\": \"" + state + "\"\n" +
                    "}";

            WebSocket.updateState(jsonString);
        } else log.error("Can't change state. Record {} not exist.", uuid);
    }
}
