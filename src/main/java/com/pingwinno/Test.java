package com.pingwinno;

import com.pingwinno.application.RecordTaskHandler;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.RecordTaskModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public class Test {
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {

        String vodId = "317517637";
        UUID uuid = UUID.fromString("0b68246f-bad7-4bf2-90a7-6f3849314875");
        RecordTaskList recordTaskList = new RecordTaskList();
        recordTaskList.addTask(new RecordTaskModel(uuid, vodId));
        System.out.println(recordTaskList.getTaskList().getFirst().getVodId());
        RecordTaskHandler.saveTask(new RecordTaskModel(uuid, vodId));

    }
}
