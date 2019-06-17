package net.streamarchive.application;

import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StreamDataModel;
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

    public static LinkedHashMap<String, String> generate(StreamDataModel model, LinkedHashMap<String, Double> chunksSet) throws IOException {
        LinkedHashMap<String, String> previewList = new LinkedHashMap<>();
        ArrayList<String> chunks = new ArrayList<>(chunksSet.keySet());
        String pathString = SettingsProperties.getRecordedStreamPath() + model.getUser() + "/" + model.getUuid();
        Files.createDirectories(Paths.get(pathString + "/animated_preview/"));
        if (chunks.size() > 10) {
            int chunkNum = 0;
            int offset = chunks.size() / 10;
            for (int i = 0; i < 10; i++) {
                chunkNum = writePreviews(previewList, chunks, pathString, chunkNum, offset, i);
            }
            return previewList;

        } else {
            int chunkNum = 0;
            log.trace("{}", chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                chunkNum = writePreviews(previewList, chunks, pathString, chunkNum, i, i);
            }
            return previewList;
        }
    }

    private static int writePreviews(LinkedHashMap<String, String> previewList, ArrayList<String> chunks, String pathString, int chunkNum, int offset, int i) throws IOException {
        String path = pathString +
                "/" + chunks.get(chunkNum).replace("-muted", "");
        log.trace(path);
        ImageIO.write(FrameGrabber.getFrame(path, 640, 360),
                "jpeg", new File(pathString + "/animated_preview/preview" + i + ".jpg"));
        chunkNum += offset;
        previewList.put(String.valueOf(i), "preview" + i + ".jpg");
        return chunkNum;
    }
}
