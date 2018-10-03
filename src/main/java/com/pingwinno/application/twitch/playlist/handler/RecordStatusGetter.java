package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RecordStatusGetter {
    public static String getRecordStatus() throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        String recordStatusString = "";
        if (jsonObj.getJSONArray("videos") != null) {
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(0);
            recordStatusString = videoObj.get("status").toString();
            httpSeviceHelper.close();
        }
        System.out.println(recordStatusString);
        return recordStatusString;
        }
}
