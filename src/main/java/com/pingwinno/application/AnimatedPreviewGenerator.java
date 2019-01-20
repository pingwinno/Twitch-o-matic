package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.AnimatedPreviewModel;
import com.pingwinno.infrastructure.models.ChunkModel;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class AnimatedPreviewGenerator {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnimatedPreviewGenerator.class.getName());
    public static LinkedList<AnimatedPreviewModel> generate(StreamExtendedDataModel model, LinkedHashSet<ChunkModel> chunksSet) throws IOException {
        LinkedList<AnimatedPreviewModel> previewList = new LinkedList<>();
        ArrayList<ChunkModel> chunks = new ArrayList<>(chunksSet);
        String pathString = SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid();
        Files.createDirectories(Paths.get(pathString + "/animated_preview/"));
        if (chunks.size() > 10) {
            int chunkNum = 0;
            int offset = chunkNum;
            for (int i = 0; i < 10; i++) {
                String path = pathString + "/" + chunks.get(chunkNum).getChunkName();
                log.trace(path);
                ImageIO.write(FrameGrabber.getFrame(path, 640, 360),
                        "jpeg", new File(pathString + "/animated_preview/preview" + i + ".jpg"));
                chunkNum += offset;
                AnimatedPreviewModel animatedPreviewModel = new AnimatedPreviewModel();
                animatedPreviewModel.setSrc("preview" + i + ".jpg");
                animatedPreviewModel.setIndex(i);
                previewList.add(animatedPreviewModel);
            }
            return previewList;

        } else {
            int chunkNum = 0;
            for (int i = 0; i < chunks.size(); i++) {
                String path = pathString + "/" + chunks.get(chunkNum).getChunkName();
                log.trace(path);
                ImageIO.write(FrameGrabber.getFrame(path, 640, 360),
                        "jpeg", new File(pathString + "/animated_preview/preview" + i + ".jpg"));
                chunkNum += i;
                AnimatedPreviewModel animatedPreviewModel = new AnimatedPreviewModel();
                animatedPreviewModel.setSrc("preview" + i + ".jpg");
                animatedPreviewModel.setIndex(i);
                previewList.add(animatedPreviewModel);
            }
            return previewList;
        }
    }
}
