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
import java.util.stream.Stream;

public class StorageHelper {

    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());
    private final static int SPACE_FOR_BEST_QUALITY = 1;
    private final static int SPACE_FOR_HIGH_QUALITY = 1;
    private final static int SPACE_FOR_MEDIUM_QUALITY = 6;
    private final static int SPACE_FOR_LOW_QUALITY = 0; //space require not calculated yet

    {
        System.out.println("WARNING! Storage check disabled! Free space is:" + checkFreeSpace() + "GB");
        System.out.println("Storage isWritable:" + FILE_PATH.canWrite());
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
                System.err.println("Folder not exist!");
                System.err.println("Try create folder...");
                if (!creatingRecordedPath()) System.exit(1);
                else System.err.println("Success!");
            } else if (!FILE_PATH.canWrite()) {
                System.err.println("Can't write in " + SettingsProperties.getRecordedStreamPath());
                System.err.println("Check permissions or change RecordedStreamPath in config.prop");
                System.exit(1);

            } else if ((checkFreeSpace() < SPACE_FOR_BEST_QUALITY) && SettingsProperties.getStreamQuality().equals("best")) {
                System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                System.err.println("For recording at least 3 hours in best quality streamlink need around 10 GB space ");
                System.err.println("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else if ((checkFreeSpace() < SPACE_FOR_HIGH_QUALITY) && SettingsProperties.getStreamQuality().equals("high")) {
                System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                System.err.println("For recording at least 3 hours in high quality streamlink need around 8 GB space ");
                System.err.println("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else if ((checkFreeSpace() < SPACE_FOR_MEDIUM_QUALITY) && SettingsProperties.getStreamQuality().equals("medium")) {
                System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
                System.err.println("For recording at least 3 hours in medium quality streamlink need around 6 GB space ");
                System.err.println("Free up space or disable free storage checking in config.prop");
                System.exit(1);
            } else System.out.println("Free space is:" + checkFreeSpace() + "GB");
        }
    }

    public static void cleanUpStorage() {
        if (SettingsProperties.getIgnoreStorageCheck().equals("true")) {
            System.out.println("Checking storage...");
            if (((checkFreeSpace() < SPACE_FOR_BEST_QUALITY) && SettingsProperties.getStreamQuality().equals("best")) ||
                    ((checkFreeSpace() < SPACE_FOR_HIGH_QUALITY) && SettingsProperties.getStreamQuality().equals("high")) ||
                    ((checkFreeSpace() < SPACE_FOR_MEDIUM_QUALITY) && SettingsProperties.getStreamQuality().equals("medium"))) {
                System.out.println("Space not enough! Try to clean up storage...");
                if (FILE_PATH.delete()) {
                    if (FILE_PATH.mkdir()) System.out.println("Success!");
                } else System.err.println("Can't clean up storage! Recording stream may be failed!");
            } else System.out.println("OK. Free space is:" + checkFreeSpace() + "GB");
        } else System.out.println("Free space is:" + checkFreeSpace() + "GB");
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
