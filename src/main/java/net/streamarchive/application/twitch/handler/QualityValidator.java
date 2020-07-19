package net.streamarchive.application.twitch.handler;

import java.util.List;
import java.util.Set;

public class QualityValidator {
    public static String validate(String quality, Set<String> availableQualities) {
        if (availableQualities.contains(quality)) {
            return quality;
        } else {
            for (String availableQuality : availableQualities) {
                //Map user defined qualities to available qualities.
                if (availableQuality.contains(quality.substring(0, 3))) {
                    return availableQuality;
                }
            }
            return null;
        }
    }
}
