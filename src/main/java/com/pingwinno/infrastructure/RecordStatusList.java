package com.pingwinno.infrastructure;

import com.pingwinno.application.JdbcHandler;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class RecordStatusList {
    private static final int LIST_SIZE = 100;
    private static LinkedList<StatusDataModel> statusList = new LinkedList<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private static JdbcHandler dataHandler = new JdbcHandler();

    public RecordStatusList() {
        statusList = dataHandler.selectAll();
    }


    synchronized public void addStatus(StatusDataModel statusDataModel) {
        log.trace("add status");
        log.trace("{}", statusDataModel);
        if ((dataHandler.selectAll().size()>LIST_SIZE)) {
            dataHandler.delete(Objects.requireNonNull(statusList.pollFirst()).getUuid().toString());
        }
        if (dataHandler.search("uuid", "uuid", statusDataModel.getUuid().toString()) == null) {
            dataHandler.insert(statusDataModel);
        } else {
            dataHandler.update(statusDataModel);
        }

        statusList.add(statusDataModel);
    }

    synchronized public void changeState(UUID uuid, State state) {
        statusList = dataHandler.selectAll();
       StatusDataModel updatedDataModel = statusList.get(statusList.indexOf(statusList.stream()
               .filter(statusDataModel -> uuid.equals(statusDataModel.getUuid()))
                .findAny()
                .orElse(null)));
       updatedDataModel.setState(state);
       dataHandler.update(updatedDataModel);
    }
}
