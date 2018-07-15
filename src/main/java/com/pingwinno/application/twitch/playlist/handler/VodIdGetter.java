package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class VodIdGetter {

    public static String getVodId() throws IOException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        JSONArray params = jsonObj.getJSONArray("videos");
        JSONObject videoObj = params.getJSONObject(0);
        String str = videoObj.get("_id").toString();
        //delete "v" from id field
        httpSeviceHelper.close();
        return str.substring(0, 0) + str.substring(1);
    }

    public static boolean getRecordStatus() throws IOException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        System.out.println(jsonObj.toString());
        JSONArray params = jsonObj.getJSONArray("videos");
        JSONObject videoObj = params.getJSONObject(0);
        System.out.println(jsonObj.toString());
        String status = videoObj.get("status").toString();
        httpSeviceHelper.close();
        return status.equals("recording");
    }

}
