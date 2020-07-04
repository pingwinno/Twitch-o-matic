package net.streamarchive.application.postprocessing;

import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
@Scope("prototype")
public class AnimatedPreviewGenerator {
    @Autowired
    SettingsProvider settingsProperties;
    @Autowired
    CommandLineExecutor commandLineExecutor;
    private org.slf4j.Logger log = LoggerFactory.getLogger(AnimatedPreviewGenerator.class.getName());

    public LinkedHashMap<String, String> generate(StreamDataModel model, LinkedHashMap<String, Double> chunksSet) throws IOException {
        LinkedHashMap<String, String> previewList = new LinkedHashMap<>();
        ArrayList<String> chunks = new ArrayList<>(chunksSet.keySet());
        String pathString = settingsProperties.getRecordedStreamPath() + model.getStreamerName() + "/" + model.getUuid();
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

    private int writePreviews(LinkedHashMap<String, String> previewList, ArrayList<String> chunks, String pathString, int chunkNum, int offset, int i) {
        String path = pathString + "/chunked/" + chunks.get(chunkNum).replace("-muted", "");
        log.trace(path);
        commandLineExecutor.setPath(pathString);
        commandLineExecutor.execute("ffmpeg", "-i", path, "-s", "640x360", "-vframes", "1", pathString +
                "/animated_preview/preview" + i + ".jpg", "-y");

        chunkNum += offset;
        previewList.put(String.valueOf(i), "preview" + i + ".jpg");
        return chunkNum;
    }
}
