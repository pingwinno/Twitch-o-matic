package com.pingwinno.application;

import com.pingwinno.domain.PostDownloadHandler;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.ChunkModel;
import com.pingwinno.infrastructure.models.LinkModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import com.pingwinno.infrastructure.models.TimelinePreviewModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class TimelinePreviewGenerator {
    public static LinkedList<TimelinePreviewModel> generate(StreamExtendedDataModel model, LinkedHashSet<ChunkModel> chunksSet)
            throws IOException {
        Files.createDirectories(Paths.get(SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid() +
                "/timeline_preview/"));
        LinkedList<TimelinePreviewModel> previewList = new LinkedList<>();
        ArrayList<ChunkModel> chunks = new ArrayList<>(chunksSet);
        int chunkNum = 0;
        double frameTime = 0.0;
        for (ChunkModel chunkModel : chunks) {

            PostDownloadHandler.handleDownloadedStream("ffmpeg", "-i",
                    SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid() + "/" + chunkModel.getChunkName(),
                    "-s", "256x144", "-vframes", "1", SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid() +
                            "/timeline_preview/preview" + chunkNum + ".jpg", "-y");
            TimelinePreviewModel timelinePreviewModel = new TimelinePreviewModel();
            LinkModel linkModel = new LinkModel();
            linkModel.setSrc("preview" + chunkNum + ".jpg");
            timelinePreviewModel.setLink(linkModel);

            timelinePreviewModel.setIndex((int) frameTime);
            frameTime += chunkModel.getTime();
            previewList.add(timelinePreviewModel);
            chunkNum++;
        }
        return previewList;

    }
}
