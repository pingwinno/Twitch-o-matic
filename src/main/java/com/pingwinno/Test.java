package com.pingwinno;

import com.pingwinno.application.twitch.playlist.handler.MasterPlaylistDownloader;
import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {

        System.out.println(VodMetadataHelper.getLastVod("olyashaa"));

    }
    }

