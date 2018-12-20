package com.pingwinno.application;

import com.pingwinno.domain.PostDownloadHandler;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.ChunkModel;
import com.pingwinno.infrastructure.models.PreviewModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class TimelinePreviewGenerator {
    public static LinkedList<PreviewModel> generate(StreamExtendedDataModel model, LinkedHashSet<ChunkModel> chunksSet)
            throws IOException {
        Files.createDirectories(Paths.get(SettingsProperties.getRecordedStreamPath() + model.getUuid() +
                "/timeline_preview/"));
        LinkedList<PreviewModel> previewList = new LinkedList<>();
        ArrayList<ChunkModel> chunks = new ArrayList<>(chunksSet);
        int chunkNum = 0;
        double frameTime = 0.0;
        for (ChunkModel chunkModel : chunks) {

            PostDownloadHandler.handleDownloadedStream("ffmpeg", "-i",
                    SettingsProperties.getRecordedStreamPath() + model.getUuid() + "/" + chunkModel.getChunkName(),
                    "-s", "256x144", "-vframes", "1", SettingsProperties.getRecordedStreamPath() + model.getUuid() +
                            "/timeline_preview/preview" + chunkNum + ".jpg", "-y");
            PreviewModel previewModel = new PreviewModel();
            previewModel.setSrc("preview" + chunkNum + ".jpg");

            previewModel.setIndex((int) frameTime);
            frameTime += chunkModel.getTime();
            previewList.add(previewModel);
            chunkNum++;
        }
        return previewList;

    }
}
