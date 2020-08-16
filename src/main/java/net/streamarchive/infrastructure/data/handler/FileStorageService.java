package net.streamarchive.infrastructure.data.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.infrastructure.SettingsProvider;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static net.streamarchive.util.UrlFormatter.format;
@Slf4j
@Service
public class FileStorageService implements StorageService {
    @Autowired
    private SettingsProvider settingsProperties;

    @Autowired
    private StorageHelper storageHelper;


    @Override
    public Long size(String streamPath, String fileName) throws IOException {
        Path filePath = Paths.get(format(settingsProperties.getRecordedStreamPath(), streamPath, fileName));
        if (Files.notExists(filePath)) {
            return -1L;
        }
        return Files.size(filePath);
    }


    @Override
    public void write(InputStream inputStream, String streamPath, String fileName) throws IOException {
        Path filePath = Paths.get(format(settingsProperties.getRecordedStreamPath(), streamPath, fileName));
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }


    @Override
    public InputStream read(String streamPath, String fileName) throws IOException {
        Path filePath = Paths.get(format(settingsProperties.getRecordedStreamPath(), streamPath, fileName));
        return Files.newInputStream(filePath);
    }

    @SneakyThrows
    @PostConstruct
    @Override
    public void initialization() {
        storageHelper.initialStorageCheck();
    }

    @Override
    public void initStreamStorage(Collection<String> qualities, String path) throws IOException {
        for (String quality : qualities) {
            Path streamPath = Paths.get(path + '/' + quality);
            log.trace("Stream path: {}", streamPath);
            if (!Files.exists(streamPath)) {
                Files.createDirectories(streamPath);
            } else {
                log.warn("Stream folder exists. Maybe it's unfinished task. " +
                        "If task can't be complete, it will be remove from task list.");
                log.info("Trying finish download...");
            }
            Files.createDirectories(streamPath);
        }
    }


    @SneakyThrows
    @Override
    public void deleteStream(UUID uuid, String streamer) {
        FileUtils.deleteDirectory(new File(format(settingsProperties.getRecordedStreamPath(), streamer, uuid.toString())));
    }

    @Override
    public UUID getUUID() {
        return storageHelper.getUuidName();
    }
}
