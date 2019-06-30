package net.streamarchive.application;

import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StorageState;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class StorageHelper {
    private final
    SettingsProperties settingsProperties;

    private org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());

    public StorageHelper(SettingsProperties settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    public Map<String, Integer> getFreeSpace() throws IOException {
        Map<String, Integer> freeSpace = new HashMap<>();
        for (String user : settingsProperties.getUsers()) {
            freeSpace.put(user, (int) (Files.getFileStore(Paths.get(settingsProperties.getRecordedStreamPath() + user)).getUsableSpace() / 1073741824));
        }
        return freeSpace;
    }

    public List<StorageState> getStorageState() throws IOException {
        List<StorageState> storageStates = new ArrayList<>();
        for (String user : settingsProperties.getUsers()) {
            StorageState storageState = new StorageState();
            storageState.setUser(user);
            storageState.setTotalStorage((int) (Files.getFileStore(Paths.get(settingsProperties.getRecordedStreamPath() + user)).getTotalSpace() / 1073741824));
            storageState.setFreeStorage((int) (Files.getFileStore(Paths.get(settingsProperties.getRecordedStreamPath() + user)).getUsableSpace() / 1073741824));
            storageStates.add(storageState);
        }
        return storageStates;
    }
    private boolean creatingRecordedPath() throws IOException {
        boolean pass = true;
        for (String user : settingsProperties.getUsers()) {
            pass = (Files.createDirectories(Paths.get(settingsProperties.getRecordedStreamPath() + user)) != null);
        }
        return pass;
    }

    @PostConstruct
    public boolean initialStorageCheck() throws IOException {
        boolean pass = true;
        for (String user : settingsProperties.getUsers()) {
            if (!Files.exists(Paths.get(settingsProperties.getRecordedStreamPath() + user)) && !Files.exists(Paths.get(settingsProperties.getRecordedStreamPath() + user))) {
                log.info("Folder not exist!");
                log.info("Try create folder...");
                if (!creatingRecordedPath()) {
                    pass = false;
                } else {
                    log.info("Success!");
                }
            } else if (!Files.isWritable(Paths.get(settingsProperties.getRecordedStreamPath() + user))) {
                log.warn("Can't write in {}", settingsProperties.getRecordedStreamPath());
                log.warn("Check permissions or change RecordedStreamPath in config_test.prop");
                pass = false;
            }
        }
        for (String user : settingsProperties.getUsers()) {
            log.info("Free space for {} is: {} GB", user, getFreeSpace().get(user));
        }
        return pass;

    }

    public UUID getUuidName() {
        UUID uuid = UUID.randomUUID();
        Path streamPath = Paths.get(settingsProperties.getRecordedStreamPath() + uuid.toString());
        if (Files.exists(streamPath) && Files.isDirectory(streamPath)) {
            //generate uuid again
            uuid = getUuidName();
        }
        return uuid;
    }
}
