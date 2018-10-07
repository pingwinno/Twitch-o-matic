package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamMetadataModel;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class VodMetadataHelper {

    public static StreamMetadataModel getLastVod() throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        System.out.println(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        String vodIdString = null;
        StreamMetadataModel streamMetadata = new StreamMetadataModel();
        if (jsonObj.getJSONArray("videos") != null) {
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(0);
            String rawVodId = videoObj.get("_id").toString();
            //delete "v" from id field
            streamMetadata.setVodId(rawVodId.substring(0, 0) + rawVodId.substring(1));
            streamMetadata.setTitle(videoObj.get("title").toString());
            streamMetadata.setDate(videoObj.get("recorded_at").toString());
            streamMetadata.setGame(videoObj.get("game").toString());
            httpSeviceHelper.close();

        }
        return streamMetadata;
        }

    public static StreamMetadataModel getVodMetadata(String vodId) throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        httpGet.addHeader("Accept","application/vnd.twitchtv.v5+json");
        JSONObject dataObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        System.out.println(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        StreamMetadataModel streamMetadata = new StreamMetadataModel();
        if (!dataObj.toString().equals("")) {
            streamMetadata.setTitle(dataObj.get("title").toString());
            streamMetadata.setDate(dataObj.get("recorded_at").toString());
            if (!dataObj.get("game").toString().equals("")) {
                streamMetadata.setGame(dataObj.get("game").toString());
            }
            else streamMetadata.setGame("");
            httpSeviceHelper.close();
        }
        return streamMetadata;
    }

}
