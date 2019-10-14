package net.streamarchive.application.twitch.handler;


public class StreamPathExtractor {
    public static String extract(String referenceURL) {
        int index = referenceURL.lastIndexOf('/');
        return referenceURL.substring(0, index);
    }
}
