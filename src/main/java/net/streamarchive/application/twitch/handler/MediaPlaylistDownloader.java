package net.streamarchive.application.twitch.handler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
@Scope("prototype")
public class MediaPlaylistDownloader {

    public BufferedReader getMediaPlaylist(String m3u8Link) throws InterruptedException, IOException {
        try {
        URL url = new URL(m3u8Link);
            URLConnection connection = null;

            connection = url.openConnection();

            return new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            if (e.getMessage().contains("403 for URL")) {
                Thread.sleep(10000);
                return getMediaPlaylist(m3u8Link);
            } else throw new IOException(e.getMessage());
        }
    }
}
