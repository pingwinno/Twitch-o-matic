package com.pingwinno.application;

import com.google.gson.Gson;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.io.*;
import java.util.LinkedList;

public class RecordTaskHandler {

    static public void loadTaskList() throws FileNotFoundException {
        FileReader fileReader = new FileReader(SettingsProperties.getRecordedStreamPath() + "recoveryList.json");
        BufferedReader reader = new BufferedReader(fileReader);
        Gson gson = new Gson();
        new RecordTaskList(gson.fromJson(fileReader,LinkedList.class));
        }

    static public void saveTask(RecordTaskModel recordTaskModel) throws IOException {
        RecordTaskList recordTaskList = new RecordTaskList();
        recordTaskList.addTask(recordTaskModel);
        Gson gson = new Gson();
        String recordTaskListString = gson.toJson(recordTaskList);
        FileWriter fileWriter = new FileWriter(SettingsProperties.getRecordedStreamPath() + "recoveryList.json");
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(recordTaskListString);
        out.flush();
        //close buffer writer
        out.close();
        fileWriter.close();
    }
    static public void removeTask(RecordTaskModel recordTaskModel) throws IOException {
        RecordTaskList recordTaskList = new RecordTaskList();
        recordTaskList.removeTask(recordTaskModel);
        Gson gson = new Gson();
        String recordTaskListString = gson.toJson(recordTaskList);
        FileWriter fileWriter = new FileWriter(SettingsProperties.getRecordedStreamPath() + "recoveryList.json");
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(recordTaskListString);
        out.flush();
        //close buffer writer
        out.close();
        fileWriter.close();
    }
}
