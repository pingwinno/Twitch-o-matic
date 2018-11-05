package com.pingwinno.infrastructure;

import com.pingwinno.domain.sqlite.handlers.SqliteStatusDataHandler;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;

public class RecordStatusList {
    private static final int LIST_SIZE = 100;
    private static LinkedList<StatusDataModel> statusList = new LinkedList<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private static SqliteStatusDataHandler dataHandler = new SqliteStatusDataHandler();

    public RecordStatusList() throws SQLException {
        statusList = dataHandler.selectAll();
    }


    synchronized public void addStatus(StatusDataModel statusDataModel) throws SQLException {
        log.trace("add status");
        log.trace("{}", statusDataModel);
        if ((dataHandler.selectAll().size()>LIST_SIZE)) {
            dataHandler.delete(Objects.requireNonNull(statusList.pollFirst()).getUuid().toString());

        }
        dataHandler.insert(statusDataModel);
        statusList.add(statusDataModel);
    }

    synchronized public LinkedList<StatusDataModel> getStatusList() {
        return new LinkedList<>(statusList);
    }

    synchronized public void changeState(String vodId, State state) throws SQLException {
        statusList = dataHandler.selectAll();
       StatusDataModel updatedDataModel = statusList.get(statusList.indexOf(statusList.stream()
                .filter(statusDataModel -> vodId.equals(statusDataModel.getVodId()))
                .findAny()
                .orElse(null)));
       updatedDataModel.setState(state);
       dataHandler.update(updatedDataModel);
    }
    synchronized public boolean isExist(String vodId) throws SQLException {
        boolean isExist ;
        statusList = dataHandler.selectAll();
        if (!statusList.isEmpty()) {
            if (statusList.indexOf(statusList.stream()
                    .filter(statusDataModel -> vodId.equals(statusDataModel.getVodId()))
                    .findAny()
                    .orElse(null)) > 0) {
                isExist = statusList.get(statusList.indexOf(statusList.stream()
                        .filter(statusDataModel -> vodId.equals(statusDataModel.getVodId()))
                        .findAny()
                        .orElse(null))) != null;
            }else {
                isExist =false;
            }
        }else {
            isExist=  false;
        }
        return isExist;
    }

}
