package net.streamarchive.application.postprocessing;

import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.models.Preview;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope("prototype")

public class TimelinePreviewGenerator {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(TimelinePreviewGenerator.class.getName());
    @Autowired
    private SettingsProvider settingsProperties;
    @Autowired
    private CommandLineExecutor commandLineExecutor;

    public LinkedHashMap<String, Preview> generate(StreamDataModel model, LinkedHashMap<String, Double> chunksSet)
            throws IOException {
        String pathString = settingsProperties.getRecordedStreamPath() + model.getStreamerName() + "/" + model.getUuid();
        Files.createDirectories(Paths.get(settingsProperties.getRecordedStreamPath() + model.getStreamerName() + "/" + model.getUuid() +
                "/timeline_preview/"));
        LinkedHashMap<String, Preview> previewList = new LinkedHashMap<>();
        int chunkNum = 0;
        double frameTime = 0.0;
        for (Map.Entry<String, Double> chunk : chunksSet.entrySet()) {
            commandLineExecutor.setPath(pathString);
            commandLineExecutor.execute("ffmpeg", "-i",
                    pathString + "/chunked/" + chunk.getKey().replace("-muted", ""),
                    "-s", "256x144", "-vframes", "1", pathString +
                            "/timeline_preview/preview" + chunkNum + ".jpg", "-y");
            frameTime += chunk.getValue();
            previewList.put(String.valueOf((int) frameTime), new Preview("preview" + chunkNum + ".jpg"));
            chunkNum++;
        }
        return previewList;

    }
}
