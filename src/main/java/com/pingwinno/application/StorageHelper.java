package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class StorageHelper {
    private final static Path STREAMS_PATH = Paths.get(SettingsProperties.getRecordedStreamPath());
    private final static Path DB_PATH = Paths.get(SettingsProperties.getSqliteFile());
    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());

    private static int checkFreeSpace() throws IOException {

        return (int) (Files.getFileStore(STREAMS_PATH).getUsableSpace() / 1073741824);
    }

    private static boolean creatingRecordedPath() throws IOException {
        return (Files.createDirectories(STREAMS_PATH) != null) && (Files.createDirectories(DB_PATH) != null);
    }
    
    public static boolean initialStorageCheck() throws IOException {
        boolean pass = true;

        if (!Files.exists(STREAMS_PATH) && !Files.exists(STREAMS_PATH)) {
            log.info("Folder not exist!");
            log.info("Try create folder...");
            if (!creatingRecordedPath()) {
                pass = false;
            } else {
                log.info("Success!");
            }
        } else if (!Files.isWritable(STREAMS_PATH)&& !Files.isWritable(DB_PATH)) {
            log.warn("Can't write in {}", SettingsProperties.getRecordedStreamPath());
            log.warn("Check permissions or change RecordedStreamPath in config_test.prop");
            pass = false;
        }
        log.info("Free space is: {} GB",checkFreeSpace());


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
