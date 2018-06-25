package com.pingwinno.application.twitch.playlist.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MediaPlaylistDownloader {
    BufferedReader reader;
    URLConnection connection;
    public  BufferedReader getMediaPlaylist (String m3u8Link) throws IOException {
        URL url = new URL(m3u8Link);
         connection = url.openConnection();
         reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
    return reader;
    }

    public void close() throws IOException {
        reader.close();
    }

}
