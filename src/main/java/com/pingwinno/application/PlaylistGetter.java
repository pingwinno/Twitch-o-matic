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


    public static void getPlaylistToken(String user) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/vod_id/272439809");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        System.out.println(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);
        System.out.println(response.getStatusLine());
        JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(json.toString());

        client.close();
        response.close();


    }

}
