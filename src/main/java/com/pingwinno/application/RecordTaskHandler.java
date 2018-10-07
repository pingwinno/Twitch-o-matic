package com.pingwinno.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.io.*;
import java.util.LinkedList;

public class RecordTaskHandler {

    static public boolean loadTaskList() throws IOException {
        FileReader fileReader = new FileReader("recoveryList.json");
        BufferedReader reader = new BufferedReader(fileReader);
        ObjectMapper mapper = new ObjectMapper();
        RecordTaskList recordTaskList = new RecordTaskList();
        try {
            recordTaskList =
                    new RecordTaskList(mapper.readValue(fileReader,
                            new TypeReference<LinkedList<RecordTaskModel>>() {
                            }));
        } catch (MismatchedInputException ignored) {
        }
        reader.close();
        return (recordTaskList.getTaskList().size() != 0);
    }

    static public void saveTask(RecordTaskModel recordTaskModel) throws IOException {
        RecordTaskList recordTaskList = new RecordTaskList();
        recordTaskList.addTask(recordTaskModel);
        ObjectMapper mapper = new ObjectMapper();
        String recordTaskListString = mapper.writeValueAsString(recordTaskList.getTaskList());
        FileWriter fileWriter = new FileWriter("recoveryList.json");
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(recordTaskListString);
        out.flush();
        out.close();
        fileWriter.close();
    }

    static public void removeTask(RecordTaskModel recordTaskModel) throws IOException {
        RecordTaskList recordTaskList = new RecordTaskList();
        recordTaskList.removeTask(recordTaskModel);
        ObjectMapper mapper = new ObjectMapper();
        String recordTaskListString = mapper.writeValueAsString(recordTaskList);
        FileWriter fileWriter = new FileWriter("recoveryList.json");
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(recordTaskListString);
        out.flush();
        out.close();
        fileWriter.close();
    }
}
