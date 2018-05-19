package com.pingwinno.application;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class UserIdGetter {

    public String getUserId(String user) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/users/" + user);
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        System.out.println(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);
        JSONObject jsonObj = new JSONObject(EntityUtils.toString(response.getEntity()));
        String userId = jsonObj.get("_id").toString();
        client.close();

        return userId;
    }
}
