package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;

import java.io.File;
import java.util.logging.Logger;

public class StorageHelper {
    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private static Logger log = Logger.getLogger(StorageHelper.class.getName());

    private static int checkFreeSpace() {

        return (int) (FILE_PATH.getFreeSpace() / 1073741824);
    }

    private static boolean creatingRecordedPath() {
        return FILE_PATH.mkdir();
    }

    public static void createChunksFolder(String streamName) {
        new File(StreamFileNameHelper.makeStreamFolderPath(streamName)).mkdir();
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
            log.warning("Can't write in " + SettingsProperties.getRecordedStreamPath());
            log.warning("Check permissions or change RecordedStreamPath in config.prop");
            pass = false;
        }
        log.info("Free space is:" + checkFreeSpace() + "GB");
        return pass;
    }

}
