package com.pingwinno.application;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class PlaylistGetter {

    private JSONObject json;
    private String sig;

    public static void getPlaylistToken(String user) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/api/videos/a270858714");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        System.out.println(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);
        System.out.println(response.getStatusLine());
        JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(json.toString());

        client.close();
        response.close();


    }

    public void getMasterPlaylist(String user) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://usher.twitch.tv/api/channel/hls/" + user + ".m3u8");
        httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        httpPost.addHeader("player", "twitchweb");
        StringEntity params = new StringEntity(json.toString());
        httpPost.setEntity(params);
        httpPost.addHeader("sig", sig);
        httpPost.addHeader("allow_audio_only", "true");
        httpPost.addHeader("allow_source","true");
        httpPost.addHeader("type","any");
        httpPost.addHeader ("p","9333029");
        System.out.println(httpPost.toString());
        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println(response.getStatusLine());
        System.out.println(response.getEntity());
        client.close();
        response.close();


    }
}
