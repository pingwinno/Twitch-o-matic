package com.pingwinno.application;

import com.pingwinno.infrastructure.SettingsProperties;

public class StreamFileNameHelper {

    public static String makeStreamName(String title, String startedAt) {
        String recordedStreamFileName = title + "-" + startedAt;
        if (recordedStreamFileName.contains(" ")) {
            recordedStreamFileName = recordedStreamFileName.replaceAll(" ", "_");
        }
        return recordedStreamFileName;
    }

    public static String makeFileName(String recordedStreamName) {
        return recordedStreamName + ".mp4";
    }

    public static String makeStreamFolderPath(String recordedStreamName) {
        return SettingsProperties.getRecordedStreamPath()
                + "/" + recordedStreamName;
    }
}
