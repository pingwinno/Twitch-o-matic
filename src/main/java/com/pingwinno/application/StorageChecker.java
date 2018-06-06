package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;

import java.io.File;

public class StorageChecker {

    private final static File FILE_PATH = new File(SettingsProperties.getRecordedStreamPath());

    public static void writeCheck()
    {
        if (!FILE_PATH.canWrite()){
            System.err.println("Can't write in " + SettingsProperties.getRecordedStreamPath());
            System.err.println("Check permissions or change RecordedStreamPath in config.prop");
            System.exit(1);
        }
    }

    public static void storageCheck()
    {
        long freeSpaseInGb = FILE_PATH.getFreeSpace() /1073741824;

        if ((freeSpaseInGb < 10) && SettingsProperties.getStreamQuality().equals("best")) {
            System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
            System.err.println("For recording at least 3 hours in best quality streamlink need around 10 GB space ");
            System.err.println("Free up space or disable free storage checking in config.prop");
            System.exit(1);
        }
       else if ((freeSpaseInGb < 8) && SettingsProperties.getStreamQuality().equals("high")) {
            System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
            System.err.println("For recording at least 3 hours in high quality streamlink need around 8 GB space ");
            System.err.println("Free up space or disable free storage checking in config.prop");
            System.exit(1);
        }
        else if ((freeSpaseInGb < 6) && SettingsProperties.getStreamQuality().equals("medium")) {
            System.err.println("Not enough space in " + SettingsProperties.getRecordedStreamPath());
            System.err.println("For recording at least 3 hours in medium quality streamlink need around 6 GB space ");
            System.err.println("Free up space or disable free storage checking in config.prop");
            System.exit(1);
        }
        else System.out.println("Free space is:" + freeSpaseInGb + "GB");

    }
}
