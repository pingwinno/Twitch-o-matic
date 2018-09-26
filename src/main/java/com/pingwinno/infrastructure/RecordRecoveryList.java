package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StreamMetadataModel;

import java.util.LinkedList;

public class RecordRecoveryList {
    private static final int LIST_SIZE = 5;
    private static LinkedList<StreamMetadataModel> recoveryList = new LinkedList<>();

    synchronized public void addStream (StreamMetadataModel streamMetadataModel)
    {
        if(recoveryList.size() > LIST_SIZE) recoveryList.removeFirst();
        recoveryList.add(streamMetadataModel);
        }

        synchronized public LinkedList<StreamMetadataModel> getRecoveryList() {
        return new LinkedList<>(recoveryList);
        }
}
