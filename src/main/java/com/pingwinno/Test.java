package com.pingwinno;

import com.pingwinno.domain.DataBaseHandler;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.RecordTaskList;
import com.pingwinno.infrastructure.models.RecordTaskModel;
import com.pingwinno.infrastructure.models.StreamMetadataModel;

import java.io.IOException;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        UUID uuid = UUID.fromString("6c225942-9a51-4f5e-b127-c28ddabb061b");
        String vodId = "317517637";
        RecordTaskModel recordTaskModel = new RecordTaskModel(uuid,vodId);
        VodDownloader vodDownloader = new VodDownloader();
        vodDownloader.initializeDownload(recordTaskModel);
    }
}
