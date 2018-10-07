package com.pingwinno;

import com.pingwinno.application.twitch.playlist.handler.VodMetadataHelper;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {

        String vodId = "317517637";
        VodMetadataHelper.getVodMetadata(vodId);

    }
}
