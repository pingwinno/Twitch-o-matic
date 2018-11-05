package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class StorageHelper {
    private final static File STREAMS_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private final static File DB_PATH = new File(SettingsProperties.getSqliteFile());
    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());

    private static int checkFreeSpace() {

        return (int) (STREAMS_PATH.getFreeSpace() / 1073741824);
    }

    private static boolean creatingRecordedPath() {
        return STREAMS_PATH.mkdir();
    }
    
    public static boolean initialStorageCheck() {
        boolean pass = true;
        log.info("Free space is:" + checkFreeSpace() + "GB");
        log.info("Storage isWritable:" + STREAMS_PATH.canWrite());
        if (!STREAMS_PATH.exists()) {
            log.info("Folder not exist!");
            log.info("Try create folder...");
            if (!creatingRecordedPath()) {
                pass = false;
            } else {
                log.info("Success!");
            }
        } else if (!STREAMS_PATH.canWrite() && !DB_PATH.canWrite()) {
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
