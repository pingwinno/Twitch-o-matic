package com.pingwinno.application.twitch.playlist.handler;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class MasterPlaylistDownloader {

    private static Logger log = Logger.getLogger(MasterPlaylistDownloader.class.getName());
    //ignore SSL because on usher.twitch.tv its broken
    CloseableHttpClient client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
    CloseableHttpResponse response;
    BufferedReader reader;
    public BufferedReader getPlaylist(String vodId) throws IOException, URISyntaxException {



        //make token request
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/api/vods/" + vodId + "/access_token");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        System.out.println(httpGet.toString());
        response = client.execute(httpGet);
        log.info("Access token request response: " + response.getStatusLine());
        JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
        //make playlist request with received token
        URIBuilder builder = new URIBuilder("https://usher.twitch.tv/vod/" + vodId);
        builder.setParameter("player", "twitchweb");
        builder.setParameter("nauth", json.get("token").toString());
        builder.setParameter("nauthsig", json.get("sig").toString());
        builder.setParameter("allow_audio_only", "true");
        builder.setParameter("allow_source", "true");
        builder.setParameter("type", "any");
        builder.setParameter("p", Integer.toString(ThreadLocalRandom.current().nextInt(1, 99998)));
        httpGet = new HttpGet(builder.build());
        response = client.execute(httpGet);
        reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        return reader;
    }
    public void close() throws IOException {
        reader.close();
        client.close();
        response.close();
    }
}
