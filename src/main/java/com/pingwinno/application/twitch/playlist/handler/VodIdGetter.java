package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class VodIdGetter {

    public static String getVodId() throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        String vodIdString = null;
        if (jsonObj.getJSONArray("videos") != null) {
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(0);
            String str = videoObj.get("_id").toString();
            //delete "v" from id field
            httpSeviceHelper.close();
            vodIdString = str.substring(0, 0) + str.substring(1);
        }
        return vodIdString;
        }


}