package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;

import java.io.File;
import java.util.logging.Logger;

public class StorageHelper {
    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private static Logger log = Logger.getLogger(StorageHelper.class.getName());

    {
        log.warning("Free space is:" + checkFreeSpace() + "GB");
        log.info("Storage isWritable:" + FILE_PATH.canWrite());
    }

    private static int checkFreeSpace() {

        return (int) (FILE_PATH.getFreeSpace() / 1073741824);
    }

    private static boolean creatingRecordedPath() {
        return FILE_PATH.mkdir();
    }

    public static void createChunksFolder(String streamName) {
        new File(StreamFileNameHelper.makeStreamFolderPath(streamName)).mkdir();
    }

    public static void initialStorageCheck() {
        if (!FILE_PATH.exists()) {
            log.warning("Folder not exist!");
            log.warning("Try create folder...");
            if (!creatingRecordedPath()) {
                System.exit(1);
            } else {
                log.warning("Success!");
            }
        } else if (!FILE_PATH.canWrite()) {
            log.warning("Can't write in " + SettingsProperties.getRecordedStreamPath());
            log.warning("Check permissions or change RecordedStreamPath in config.prop");
            System.exit(1);
        }
        log.info("Free space is:" + checkFreeSpace() + "GB");
    }

    //storage cleaner for low capacity servers
    public static void deleteUploadedFile(String file) {
        File recordedStream = new File(file);
        if (SettingsProperties.getDeleteFileAfterUpload().equals("true")) {
            recordedStream.delete();
            log.info("Free space is:" + checkFreeSpace() + "GB");
        }
    }
}
