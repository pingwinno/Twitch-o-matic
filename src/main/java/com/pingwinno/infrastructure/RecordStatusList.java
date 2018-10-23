package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class RecordStatusList {
    private static final int LIST_SIZE = 100;
    private static LinkedList<StatusDataModel> statusList = new LinkedList<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public RecordStatusList() {
    }

    public RecordStatusList(LinkedList<StatusDataModel> statusList) {
        log.trace("create status object");
        log.trace("{}", statusList);
        RecordStatusList.statusList = statusList;
    }

    synchronized public void addStatus(StatusDataModel statusDataModel) {
        log.trace("add status");
        log.trace("{}", statusDataModel);
        if ((statusList.size() != 0) && (statusList.size() > LIST_SIZE)) statusList.removeFirst();
        statusList.add(statusDataModel);
    }

    synchronized public LinkedList<StatusDataModel> getStatusList() {
        return new LinkedList<>(statusList);
    }

    synchronized public void changeState(String vodId, State state) {

        statusList.get(statusList.indexOf(statusList.stream()
                .filter(statusDataModel -> vodId.equals(statusDataModel.getVodId()))
                .findAny()
                .orElse(null))).setState(state);

    }

}
