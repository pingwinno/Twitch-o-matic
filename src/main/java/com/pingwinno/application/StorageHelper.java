package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class StorageHelper {
    private static Logger log = Logger.getLogger(StorageHelper.class.getName());
    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private final static int SPACE_FOR_BEST_QUALITY = 1;
    private final static int SPACE_FOR_HIGH_QUALITY = 1;
    private final static int SPACE_FOR_MEDIUM_QUALITY = 6;
    private final static int SPACE_FOR_LOW_QUALITY = 0; //space require not calculated yet

    {
        log.warning("WARNING! Storage check disabled! Free space is:" + checkFreeSpace() + "GB");
        log.info("Storage isWritable:" + FILE_PATH.canWrite());
    }

    private static int checkFreeSpace() {
        int freeSpaseInGb = (int) (FILE_PATH.getFreeSpace() / 1073741824);

        return freeSpaseInGb;
    }

    public static boolean creatingRecordedPath() {

        return FILE_PATH.mkdir();
    }

    public static void initialStorageCheck() {
        if (SettingsProperties.getIgnoreStorageCheck().equals("false")) {
            if (!FILE_PATH.exists()) {
                log.warning("Folder not exist!");
                log.warning("Try create folder...");
                if (!creatingRecordedPath()) System.exit(1);
                else log.warning("Success!");
            } else if (!FILE_PATH.canWrite()) {
                log.warning("Can't write in " + SettingsProperties.getRecordedStreamPath());
                log.warning("Check permissions or change RecordedStreamPath in config.prop");
                System.exit(1);

            } else if ((checkFreeSpace() < SPACE_FOR_BEST_QUALITY) && SettingsProperties.getStreamQuality().equals("best")) {
                log.warning("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                log.warning("For recording at least 3 hours in best quality streamlink need around 10 GB space ");
                log.warning("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else if ((checkFreeSpace() < SPACE_FOR_HIGH_QUALITY) && SettingsProperties.getStreamQuality().equals("high")) {
                log.warning("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                log.warning("For recording at least 3 hours in high quality streamlink need around 8 GB space ");
                log.warning("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else if ((checkFreeSpace() < SPACE_FOR_MEDIUM_QUALITY) && SettingsProperties.getStreamQuality().equals("medium")) {
                log.warning("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                log.warning("For recording at least 3 hours in medium quality streamlink need around 6 GB space ");
                log.warning("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else log.info("Free space is:" + checkFreeSpace() + "GB");
        }
    }

    public static void cleanUpStorage() {
        if (SettingsProperties.getIgnoreStorageCheck().equals("true")) {
            log.info("Checking storage...");
            if (((checkFreeSpace() < SPACE_FOR_BEST_QUALITY) && SettingsProperties.getStreamQuality().equals("best")) ||
                    ((checkFreeSpace() < SPACE_FOR_HIGH_QUALITY) && SettingsProperties.getStreamQuality().equals("high")) ||
                    ((checkFreeSpace() < SPACE_FOR_MEDIUM_QUALITY) && SettingsProperties.getStreamQuality().equals("medium"))) {
                log.info("Space not enough! Try to clean up storage...");
                if (FILE_PATH.delete()) {
                    if (FILE_PATH.mkdir()) log.info("Success!");
                } else log.warning("Can't clean up storage! Recording stream may be failed!");
            } else log.info("OK. Free space is:" + checkFreeSpace() + "GB");
        } else log.info("Free space is:" + checkFreeSpace() + "GB");
    }

    public static List<String> listOfChunksInFolder() throws IOException {

        try (Stream<Path> paths = Files.walk(Paths.get(SettingsProperties.getRecordedStreamPath()))) {

            List<String> listOfChunks = new LinkedList<String>() {
            };
            paths.filter(Files::isRegularFile).forEach(path -> listOfChunks.add(path.toString()));

            Collections.sort(listOfChunks);
            return listOfChunks;
        }
    }
}
