package com.pingwinno;

import com.pingwinno.application.twitch.playlist.handler.MasterPlaylistDownloader;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) {
        System.out.println(SettingsProperties.getStreamQuality());
    }
}

