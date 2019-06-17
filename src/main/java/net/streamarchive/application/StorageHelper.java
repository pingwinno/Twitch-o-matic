package net.streamarchive.application;

import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StorageState;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class StorageHelper {
    private final static String STREAMS_PATH = SettingsProperties.getRecordedStreamPath();
    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());

    public static Map<String, Integer> getFreeSpace() throws IOException {
        Map<String, Integer> freeSpace = new HashMap<>();
        for (String user : SettingsProperties.getUsers()) {
            freeSpace.put(user, (int) (Files.getFileStore(Paths.get(STREAMS_PATH + user)).getUsableSpace() / 1073741824));
        }
        return freeSpace;
    }

    public static List<StorageState> getStorageState() throws IOException {
        List<StorageState> storageStates = new ArrayList<>();
        for (String user : SettingsProperties.getUsers()) {
            StorageState storageState = new StorageState();
            storageState.setUser(user);
            storageState.setTotalStorage((int) (Files.getFileStore(Paths.get(STREAMS_PATH + user)).getTotalSpace() / 1073741824));
            storageState.setFreeStorage((int) (Files.getFileStore(Paths.get(STREAMS_PATH + user)).getUsableSpace() / 1073741824));
            storageStates.add(storageState);
        }
        return storageStates;
    }

    private static boolean creatingRecordedPath() throws IOException {
        boolean pass = true;
        for (String user : SettingsProperties.getUsers()) {
            pass = (Files.createDirectories(Paths.get(STREAMS_PATH + user)) != null);
        }
        return pass;
    }
    
    public static boolean initialStorageCheck() throws IOException {
        boolean pass = true;
        for (String user : SettingsProperties.getUsers()) {
            if (!Files.exists(Paths.get(STREAMS_PATH + user)) && !Files.exists(Paths.get(STREAMS_PATH + user))) {
                log.info("Folder not exist!");
                log.info("Try create folder...");
                if (!creatingRecordedPath()) {
                    pass = false;
                } else {
                    log.info("Success!");
                }
            } else if (!Files.isWritable(Paths.get(STREAMS_PATH + user))) {
                log.warn("Can't write in {}", SettingsProperties.getRecordedStreamPath());
                log.warn("Check permissions or change RecordedStreamPath in config_test.prop");
                pass = false;
            }

            log.info("Free space is: {} GB", getFreeSpace().get(user));

        }
        return pass;

    }

    public static UUID getUuidName(){
        UUID uuid = UUID.randomUUID();
        Path streamPath = Paths.get(SettingsProperties.getRecordedStreamPath() + uuid.toString());
        if(Files.exists(streamPath) && Files.isDirectory(streamPath)){
            //generate uuid again
         uuid = getUuidName();
        }
            return uuid;
        }
}
