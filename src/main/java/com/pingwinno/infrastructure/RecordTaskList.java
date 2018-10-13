package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class RecordTaskList {
    private static final int LIST_SIZE = 10;
    private static LinkedList<StreamExtendedDataModel> taskList = new LinkedList<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public RecordTaskList() {
    }

    public RecordTaskList(LinkedList<StreamExtendedDataModel> taskList) {
        log.trace("create task object");
        log.trace("{}", taskList);
        RecordTaskList.taskList = taskList;
    }

    synchronized public void addTask(StreamExtendedDataModel streamExtendedDataModel) {
        log.trace("add task");
        log.trace("{}", streamExtendedDataModel);
        if ((taskList.size() != 0) && (taskList.size() > LIST_SIZE)) taskList.removeFirst();
        taskList.add(streamExtendedDataModel);
    }

    synchronized public LinkedList<StreamExtendedDataModel> getTaskList() {
        return new LinkedList<>(taskList);
    }

    synchronized public boolean removeTask(StreamExtendedDataModel recordTask) {
        boolean isRemoved = false;
        log.trace("remove task");
        if (taskList.contains(recordTask)) {
            taskList.remove(recordTask);
            isRemoved = true;
        }
        log.trace("{} isRemoved: {}", recordTask, isRemoved);
        return isRemoved;
    }
}
