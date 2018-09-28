package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.util.LinkedList;

public class RecordTaskList {
    private static final int LIST_SIZE = 10;
    private static LinkedList<RecordTaskModel> taskList;

    public RecordTaskList() {
    }

    public RecordTaskList(LinkedList<RecordTaskModel> taskList) {
        RecordTaskList.taskList = taskList;
    }

    synchronized public void addTask(RecordTaskModel RecordTaskModel) {
        if (taskList.size() > LIST_SIZE) taskList.removeFirst();
        taskList.add(RecordTaskModel);
    }

    synchronized public LinkedList<RecordTaskModel> getTaskList() {
        return new LinkedList<>(taskList);
    }

    synchronized public boolean removeTask(RecordTaskModel recordTask) {
        boolean flag = true;
        if (taskList.contains(recordTask)) {
            taskList.remove(recordTask);
            flag = false;
        }
        return flag;
    }
}
