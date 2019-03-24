package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AnimatedPreviewGenerator {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnimatedPreviewGenerator.class.getName());

    public static LinkedHashMap<Integer, String> generate(StreamExtendedDataModel model, LinkedHashMap<String, Double> chunksSet) throws IOException {
        LinkedHashMap<Integer, String> previewList = new LinkedHashMap<>();
        ArrayList<String> chunks = new ArrayList<>(chunksSet.keySet());
        String pathString = SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid();
        Files.createDirectories(Paths.get(pathString + "/animated_preview/"));
        if (chunks.size() > 10) {
            int chunkNum = 0;
            int offset = chunks.size() / 10;
            for (int i = 0; i < 10; i++) {
                String path = pathString +
                        "/" + chunks.get(chunkNum).replace("-muted", "");
                log.trace(path);
                ImageIO.write(FrameGrabber.getFrame(path, 640, 360),
                        "jpeg", new File(pathString + "/animated_preview/preview" + i + ".jpg"));
                chunkNum += offset;
                previewList.put(i, "preview" + i + ".jpg");
            }
            return previewList;

        } else {
            int chunkNum = 0;
            log.trace("{}", chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                String path = pathString +
                        "/" + chunks.get(chunkNum).replace("-muted", "");
                log.trace(path);
                ImageIO.write(FrameGrabber.getFrame(path, 640, 360),
                        "jpeg", new File(pathString + "/animated_preview/preview" + i + ".jpg"));
                chunkNum += i;
                previewList.put(i, "preview" + i + ".jpg");
            }
            return previewList;
        }
    }
}
