package com.pingwinno.application;

public class StreamFileNameHelper {

    public static String makeFileName(String title, String startedAt)
    {
        String recordedStreamFileName = title + "-" + startedAt + ".mp4";
        if (recordedStreamFileName.contains(" ")) {
            recordedStreamFileName = recordedStreamFileName.replaceAll(" ", "_");
        }
        return recordedStreamFileName;
    }
}
