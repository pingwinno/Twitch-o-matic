package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StreamExtendedDataModel;

import java.util.LinkedList;

public class RecordTaskList {
    private static final int LIST_SIZE = 10;
    private static LinkedList<StreamExtendedDataModel> taskList = new LinkedList<>();

    public RecordTaskList() {
    }

    public RecordTaskList(LinkedList<StreamExtendedDataModel> taskList) {
        RecordTaskList.taskList = taskList;
    }

    synchronized public void addTask(StreamExtendedDataModel StreamExtendedDataModel) {
        if ((taskList.size() != 0) && (taskList.size() > LIST_SIZE)) taskList.removeFirst();
        taskList.add(StreamExtendedDataModel);
    }

    synchronized public LinkedList<StreamExtendedDataModel> getTaskList() {
        return new LinkedList<>(taskList);
    }

    synchronized public boolean removeTask(StreamExtendedDataModel recordTask) {
        boolean flag = true;
        if (taskList.contains(recordTask)) {
            taskList.remove(recordTask);
            flag = false;
        }
        return flag;
    }
}
