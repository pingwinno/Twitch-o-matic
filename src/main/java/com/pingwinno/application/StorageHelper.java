package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

public class StorageHelper {
    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());

    private static int checkFreeSpace() {

        return (int) (FILE_PATH.getFreeSpace() / 1073741824);
    }

    private static boolean creatingRecordedPath() {
        return FILE_PATH.mkdir();
    }
    
    public static boolean initialStorageCheck() {
        boolean pass = true;
        log.info("Free space is:" + checkFreeSpace() + "GB");
        log.info("Storage isWritable:" + FILE_PATH.canWrite());
        if (!FILE_PATH.exists()) {
            log.info("Folder not exist!");
            log.info("Try create folder...");
            if (!creatingRecordedPath()) {
                pass = false;
            } else {
                log.info("Success!");
            }
        } else if (!FILE_PATH.canWrite()) {
            log.warn("Can't write in " + SettingsProperties.getRecordedStreamPath());
            log.warn("Check permissions or change RecordedStreamPath in config_test.prop");
            pass = false;
        }
        log.info("Free space is:" + checkFreeSpace() + "GB");
        return pass;
    }

    public static UUID getUuidName(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
       String streamFilePath = SettingsProperties.getRecordedStreamPath()
                + uuidString;
        File f = new File(streamFilePath);
        if(f.exists() && f.isDirectory()){
         uuid = getUuidName();
        }
            return uuid;
        }

}
