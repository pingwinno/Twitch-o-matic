package net.streamarchive.application.twitch.handler;

import java.util.Map;

public class QualityValidator {
    public static String validate(String quality, Map<String, String> availableQualities) {
        if (availableQualities.containsKey(quality)) {
            return quality;
        } else {
            for (String availableQuality : availableQualities.keySet()) {
                if (availableQuality.contains(quality)) {
                    return availableQuality;
                }
            }
            return "chunked";
        }
    }
}
