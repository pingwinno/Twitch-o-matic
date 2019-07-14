package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.HttpSevice;
import net.streamarchive.infrastructure.StreamNotFoundExeption;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class VodMetadataHelperTest {

    @Test
    void getLastVod() throws InterruptedException, StreamNotFoundExeption, IOException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/dedlim/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        System.out.println(httpSevice.getService(httpGet, true).getEntity());

    }

}