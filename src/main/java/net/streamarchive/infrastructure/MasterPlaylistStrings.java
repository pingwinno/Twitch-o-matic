package net.streamarchive.infrastructure;

import java.util.Map;

public class MasterPlaylistStrings {
    private static final Map<String, String> bandwidth = Map.of(
            "chunked", "#EXT-X-STREAM-INF:BANDWIDTH=5379505,CODECS=\"avc1.64002A,mp4a.40.2\",RESOLUTION=1920x1080,FRAME-RATE=59.998\n",
            "720p60", "#EXT-X-STREAM-INF:BANDWIDTH=3061974,CODECS=\"avc1.4D401F,mp4a.40.2\",RESOLUTION=1280x720,FRAME-RATE=59.998\n",
            "720p30", "#EXT-X-STREAM-INF:BANDWIDTH=2206798,CODECS=\"avc1.4D401F,mp4a.40.2\",RESOLUTION=1280x720,FRAME-RATE=29.997\n",
            "480p30", "#EXT-X-STREAM-INF:BANDWIDTH=1412348,CODECS=\"avc1.4D401E,mp4a.40.2\",RESOLUTION=852x480,FRAME-RATE=29.997\n",
            "360p30", "#EXT-X-STREAM-INF:BANDWIDTH=695399,CODECS=\"avc1.4D401E,mp4a.40.2\",RESOLUTION=640x360,FRAME-RATE=29.997\n",
            "160p30", "#EXT-X-STREAM-INF:BANDWIDTH=284967,CODECS=\"avc1.4D400C,mp4a.40.2\",RESOLUTION=284x160,FRAME-RATE=29.997\n");

    public static String getStreamHeader(String quality) {
        return bandwidth.get(quality);
    }
}
