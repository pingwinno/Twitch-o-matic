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

public class AnimatedPreviewGenerator {
    public static LinkedList<PreviewModel> generate(StreamExtendedDataModel model, LinkedHashSet<ChunkModel> chunksSet) throws IOException {
        LinkedList<PreviewModel> previewList = new LinkedList<>();
        ArrayList<ChunkModel> chunks = new ArrayList<>(chunksSet);
        Files.createDirectories(Paths.get(SettingsProperties.getRecordedStreamPath() + model.getUser() + model.getUuid() +
                "/animated_preview/"));
        if (chunks.size() > 10) {
            int chunkNum = chunks.size() / 10;
            int addNum = chunkNum;
            for (int i = 0; i < 9; i++) {
                PostDownloadHandler.handleDownloadedStream("ffmpeg", "-i",
                        SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid() + "/" + chunks.get(chunkNum).getChunkName(),
                        "-s", "640x360", "-vframes", "1", SettingsProperties.getRecordedStreamPath() + model.getUuid() +
                                "/animated_preview/preview" + i + ".jpg", "-y");
                chunkNum = chunkNum + addNum;
                PreviewModel previewModel = new PreviewModel();
                previewModel.setSrc("preview" + i + ".jpg");
                previewModel.setIndex(i);
                previewList.add(previewModel);
            }
            return previewList;

        } else {
            int chunkNum = chunks.size();
            for (int i = 0; i < chunkNum; i++) {
                PostDownloadHandler.handleDownloadedStream("ffmpeg", "-i",
                        SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid() + "/" + chunks.get(chunkNum).getChunkName(),
                        "-s", "640x360", "-vframes", "1", SettingsProperties.getRecordedStreamPath() + model.getUuid() +
                                "/animated_preview/preview" + i + ".jpg", "-y");
                chunkNum = chunkNum + i;
                PreviewModel previewModel = new PreviewModel();
                previewModel.setSrc("preview" + i + ".jpg");
                previewModel.setIndex(i);
                previewList.add(previewModel);
            }
            return previewList;
        }
    }
}
