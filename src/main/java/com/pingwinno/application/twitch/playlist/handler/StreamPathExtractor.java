package com.pingwinno.application.twitch.playlist.handler;

import java.net.URL;

public class StreamPathExtractor {
    public static String extract (String referenceURL) {
        int index = referenceURL.lastIndexOf('/');
        return referenceURL.substring(0, index);
    }
}
